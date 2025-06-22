package com.project.bookommendbe.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.bookommendbe.db.*;
import com.project.bookommendbe.dto.*;
import com.project.bookommendbe.dto.api.library.Doc;
import com.project.bookommendbe.dto.api.library.Result;
import com.project.bookommendbe.entity.*;
import com.project.bookommendbe.dto.api.naver.Channel;
import com.project.bookommendbe.dto.api.naver.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/*
* c -> create
* r -> read
* u -> update
* d -> delete
* */

@Slf4j
@RestController
public class UserBookController {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final UserBookRepository userBookRepository;
    private final ReviewRepository reviewRepository;
    private final ReadingRecordRepository readingRecordRepository;

    @Value("${naver.clientId}")
    private String clientId;

    @Value("${naver.secretId}")
    private String secretId;

    @Value("${library.key}")
    private String libraryKey;

    @Autowired
    public UserBookController(UserRepository userRepository, BookRepository bookRepository, UserBookRepository userBookRepository, ReviewRepository reviewRepository, ReadingRecordRepository readingRecordRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.userBookRepository= userBookRepository;
        this.reviewRepository = reviewRepository;
        this.readingRecordRepository = readingRecordRepository;

    }

    @GetMapping(value = "/r1/searchBookInfo" )
    public List<BookVO> requestUserBook(@RequestParam  Map<String, String> paramMap) throws MalformedURLException {
        List<BookVO> showBooks = new ArrayList<>();
        String title  = paramMap.get("query");

        if(title!=null&& !title.isEmpty()) {

            RestTemplate restTemplate = new RestTemplate();

            List<Book> ownBookAfterSave = bookRepository.findBooksByTitleContaining(title);

            if (ownBookAfterSave != null && !ownBookAfterSave.isEmpty()) {
                ownSearchBook(showBooks, ownBookAfterSave);
                log.info("beforeApi::::");
                return showBooks;
            }

            String urlNaverString = "https://openapi.naver.com";
            URL urlNaver = new URL(urlNaverString);
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Naver-Client-Id", clientId);
            headers.add("X-Naver-Client-Secret", secretId);

            UriComponents uriComponentsNaver = UriComponentsBuilder.newInstance()
                    .scheme(urlNaver.getProtocol())
                    .host(urlNaver.getHost())
                    .path("/v1/search/book.json")
                    .queryParam("query", paramMap.get("query"))
                    .queryParam("start", paramMap.get("start"))
                    .queryParam("display", 10)
                    .build();
            String uriNaverTostring = uriComponentsNaver.toUriString();

            HttpEntity<Channel> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Channel> response = restTemplate.exchange(uriNaverTostring, HttpMethod.GET, requestEntity, Channel.class);

            if (response.getBody() != null) {

                for (Item item : response.getBody().getItems()) {

                    Optional<Book> isOwnIsbnBook = ownData(item.getIsbn());

                    if (isOwnIsbnBook.isEmpty() || !isOwnIsbnBook.get().getBookIsbn().equals(item.getIsbn())) {

                        Book saveBook = new Book();
                        saveBook.setCoverImageUrl(item.getImage());
                        saveBook.setAuthor(item.getAuthor());
                        saveBook.setBookIsbn(item.getIsbn());
                        saveBook.setTitle(item.getTitle());
                        saveBook.setDescription(item.getDescription());
                        saveBook.setPublisher(item.getPublisher());
                        saveBook.setSplitTitle(split(item.getTitle()));
                        saveBook.setPublishedDate(item.getPubdate());
                        saveBook.setPublishedDate(item.getPubdate());
                        saveBook.setDiscount(String.valueOf(item.getDiscount()));
                        bookRepository.save(saveBook);
                    }
                }
            }

            String ownTitleAfterApi = split(paramMap.get("query"));
            List<Book> ownBookAfterSaveAfterApi = bookRepository.findBooksBySplitTitleContaining(ownTitleAfterApi);

            if (ownBookAfterSaveAfterApi != null && !ownBookAfterSaveAfterApi.isEmpty()) {
                ownSearchBook(showBooks, ownBookAfterSaveAfterApi);
                log.info("AfterApi::::");
            }
        }

        return showBooks;
    }

    private void ownSearchBook(List<BookVO> showBooks, List<Book> ownBookAfterSaveAfterApi) {
        for (Book item : ownBookAfterSaveAfterApi) {
            BookVO showBook = new BookVO();
            showBook.setId(item.getId());
            showBook.setCoverImageUrl(item.getCoverImageUrl());
            showBook.setAuthor(item.getAuthor());
            showBook.setBookIsbn(item.getBookIsbn());
            showBook.setTitle(item.getTitle());
            showBook.setDescription(item.getDescription());
            showBook.setPublisher(item.getPublisher());
            showBook.setPublishedDate(item.getPublishedDate());
            showBooks.add(showBook);
        }

    }

    //가지고 있는지 확인
    private Optional<Book> ownData(String isbn){

        return bookRepository.findBookByBookIsbn(isbn);
    }

    // 여기서 쪼개고
    private String split(String query) {
        query=query.replaceAll("[^가-힣]", "");

        String[] chosungs = {"ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ", "ㅆ", "ㅇ" , "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"};
        String[] jungsungs = {"ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅓ", "ㅔ", "ㅕ", "ㅖ", "ㅗ", "ㅘ", "ㅙ", "ㅚ", "ㅛ", "ㅜ", "ㅝ", "ㅞ", "ㅟ", "ㅠ", "ㅡ", "ㅢ", "ㅣ"};
        String[] jongsungs = {"", "ㄱ", "ㄲ", "ㄳ", "ㄴ", "ㄵ", "ㄶ", "ㄷ", "ㄹ", "ㄺ", "ㄻ", "ㄼ", "ㄽ", "ㄾ", "ㄿ", "ㅀ", "ㅁ", "ㅂ", "ㅄ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"};
        String result = "";

        for (int i = 0; i < query.length(); i++) {
            int keywordUniBase = query.charAt(i) - 44032;
            char chosung = (char)(keywordUniBase / 28 / 21);
            char jungsung = (char)(keywordUniBase / 28 % 21);
            char jongsung = (char)(keywordUniBase % 28);
            result += chosungs[chosung] + jungsungs[jungsung] + jongsungs[jongsung];
        }

        System.out.println(result);
        return result;

    }

    @GetMapping("/r1/userBooks/{userId}")
    public List<UserBookReadVO> getUserBook(@PathVariable long userId) throws JsonProcessingException, MalformedURLException {

        log.error("userId:{}", userId);
        List<UserBookReadVO> userBookReadVOList = new ArrayList<>();
        // 내 책장에서 데이터 가져오기
        List<UserBook> userBooks =userBookRepository.findUserBooksByUserId(userId);

        for (UserBook userBook : userBooks) {
            Optional<Book> book = bookRepository.findBookByBookIsbn(userBook.getBookIsbn());

            log.error("userBook{}",userBook);
            if(book.isPresent()) {
                UserBookReadVO userBookReadVO = new UserBookReadVO();
                userBookReadVO.setBookIsbn(userBook.getBookIsbn());
                userBookReadVO.setTitle(book.get().getTitle());
                userBookReadVO.setAuthor(book.get().getAuthor());
                userBookReadVO.setPublisher(book.get().getPublisher());
                userBookReadVO.setCoverImageUrl(book.get().getCoverImageUrl());
                userBookReadVO.setPublishedDate(book.get().getPublishedDate());
                userBookReadVO.setDescription(book.get().getDescription());
                userBookReadVO.setBookId(book.get().getId());
                userBookReadVO.setUserBookId(userBook.getId());
                userBookReadVO.setUserId(userId);
                userBookReadVO.setPageCount(userBook.getPageCount());
                userBookReadVO.setFromPage(userBook.getFromPage());

                userBookReadVOList.add(userBookReadVO);

            }
        }

        return new ArrayList<>(userBookReadVOList);

    }

    @PostMapping("/c1/userBook")
    public void insertUserBookBy( @RequestBody UserBookSaveVO request) throws MalformedURLException {

        RestTemplate restTemplate = new RestTemplate();
        Optional<User> user = userRepository.findById(request.getUserId());

        if(user.isPresent()) {
            Optional<Book> book =bookRepository.findBookByBookIsbn(request.getBookIsbn());
            log.error("장바구니 북 :::{}",book.isPresent());
            boolean isOwnBook=userBookRepository.existsUserBookByUserAndBookIsbn(user.get(),request.getBookIsbn());

            String urlLibraryString = "https://www.nl.go.kr";
            URL urlLibrary = new URL(urlLibraryString);
            UriComponents uriComponentsLibrary = UriComponentsBuilder.newInstance()
                    .scheme(urlLibrary.getProtocol())
                    .host(urlLibrary.getHost())
                    .path("/seoji/SearchApi.do")
                    .queryParam("cert_key", libraryKey)
                    .queryParam("result_style", "json")
                    .queryParam("page_no", 1)
                    .queryParam("page_size", 10)
                    .queryParam("isbn", request.getBookIsbn())
                    .build();
            String uriLibrary = uriComponentsLibrary.toUriString();

            ResponseEntity<Result> result = restTemplate.getForEntity(uriLibrary, Result.class);


            if (result.getBody() != null) {
                if (result.getBody().getDocs() != null) {

                    for (Doc doc : result.getBody().getDocs()) {
                        log.info("category:{}", doc.getSubject());
                        book.get().setBookCategory(BookCategory.fromCode(doc.getSubject()));
                        bookRepository.save(book.get());
                    }
                }
            }

            if(!isOwnBook) {
                UserBook userBook = new UserBook();
                userBook.setBook(book.get());
                userBook.setUser(user.get());
                userBook.setBookIsbn(book.get().getBookIsbn());
                userBookRepository.save(userBook);
            }
        }
    }
    @PutMapping("/u1/userBookPageCount/{userId}/{userBookId}")
    public void updateUserBookPageCountBy(@PathVariable Long userId, @PathVariable Long userBookId, @RequestBody Map<String,String> request) {
        Optional<UserBook> userBook=userBookRepository.findUserBookByUserIdAndId(userId,userBookId);

        if(userBook.isPresent()) {
            int pageCount = Integer.parseInt(request.get("pageCount"));
            int fromPage = Integer.parseInt(request.get("fromPage"));
            userBook.get().setPageCount(pageCount);
            userBook.get().setFromPage(fromPage);
            userBookRepository.save(userBook.get());
        }
    }

    @PostMapping("/c1/userBookRecordAndReview")
    public void saveUserBookRecordAndReviewBy(@RequestBody SavingRecordAndReviewVO saveRequest) {

        log.info("saveRequest::::::{}",saveRequest);

        Optional<UserBook> userBook = userBookRepository.findUserBookByUserIdAndId(saveRequest.getUserId(), saveRequest.getUserBookId());

        if(userBook.isPresent()) {
            Review review = new Review();
            ReadingRecord readingRecord = new ReadingRecord();


            review.setCreatedAt(LocalDateTime.now());
            review.setReviewDate(saveRequest.getRecord().getDate());
            review.setRating(RatingEnum.fromValue(saveRequest.getRating()));
            review.setUser(userBook.get().getUser());
            review.setBook(userBook.get().getBook());
            reviewRepository.save(review);

            readingRecord.setUserBook(userBook.get());
            readingRecord.setUser(userBook.get().getUser());

            readingRecord.setBookIsbn(saveRequest.getRecord().getBookIsbn());
            readingRecord.setToPage(saveRequest.getRecord().getToPage());
            readingRecord.setFromPage(saveRequest.getRecord().getFromPage());
            readingRecord.setDate(saveRequest.getRecord().getDate());
            readingRecord.setComment(saveRequest.getRecord().getComment());
            readingRecord.setOpinion(saveRequest.getRecord().getOpinion());
            readingRecord.setTime(saveRequest.getRecord().getTime());
            readingRecord.setPercent(saveRequest.getRecord().getPercent());
            readingRecord.setReadAmountCount(saveRequest.getRecord().getReadAmountCount());

            if(saveRequest.getRecord().getFromPage()==0) {
                readingRecord.setStatus(String.valueOf(ReadingStatus.TO_READ));
            }else  if(saveRequest.getRecord().getFromPage()>0) {
                readingRecord.setStatus(String.valueOf(ReadingStatus.READING));
            }
            if (saveRequest.getRecord().getFromPage()== saveRequest.getRecord().getToPage()) {
                readingRecord.setStatus(String.valueOf(ReadingStatus.COMPLETED));
            }

            readingRecordRepository.save(readingRecord);
            userBook.get().setFromPage(saveRequest.getRecord().getFromPage());
            userBookRepository.save(userBook.get());
        }
    }
}
