package com.project.bookommendbe.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.bookommendbe.dto.UserBookReadVO;
import com.project.bookommendbe.dto.UserBookSaveVO;
import com.project.bookommendbe.dto.api.library.Doc;
import com.project.bookommendbe.dto.api.library.Result;
import com.project.bookommendbe.entity.Book;
import com.project.bookommendbe.entity.BookCategory;
import com.project.bookommendbe.entity.User;
import com.project.bookommendbe.entity.UserBook;
import com.project.bookommendbe.db.BookRepository;
import com.project.bookommendbe.db.UserBookRepository;
import com.project.bookommendbe.db.UserRepository;
import com.project.bookommendbe.dto.BookVO;
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

    private UserRepository userRepository;
    private BookRepository bookRepository;
    private UserBookRepository userBookRepository;

    @Value("${naver.clientId}")
    private String clientId;

    @Value("${naver.secretId}")
    private String secretId;

    @Value("${aladin.key}")
    private String libraryKey;

    @Autowired
    public UserBookController(UserRepository userRepository, BookRepository bookRepository, UserBookRepository userBookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.userBookRepository= userBookRepository;

    }

    @GetMapping(value = "/r1/bookInfo" )
    public List<BookVO> requestUserBook(@RequestParam  Map<String, String> paramMap) throws MalformedURLException {
        List<BookVO> showBooks = new ArrayList<>();
        String title  = paramMap.get("query");
        List<Book>  ownBookAfterSave=bookRepository.findBooksByTitleContaining(title);

        if(ownBookAfterSave!=null && !ownBookAfterSave.isEmpty()) {
            OwnSearchBook(showBooks, ownBookAfterSave);
            log.info("beforeApi::::");
            return showBooks;
        }


        RestTemplate restTemplate = new RestTemplate();


        String urlString = "https://openapi.naver.com";
        URL url = new URL(urlString);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Naver-Client-Id",clientId);
        headers.add("X-Naver-Client-Secret",secretId);

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(url.getProtocol())
                .host(url.getHost())
                .path("/v1/search/book.json")
                .queryParam("query",paramMap.get("query"))
                .queryParam("start",paramMap.get("start"))
                .queryParam("display",10)
                .build();
        String uriNaver = uriComponents.toUriString();

        HttpEntity<Channel> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Channel> response = restTemplate.exchange(uriNaver, HttpMethod.GET, requestEntity, Channel.class);

        if(response.getBody() != null) {

            for (Item item : response.getBody().getItems()) {

                Optional<Book> isOwnIsbnBook = ownData(item.getIsbn());

                if (isOwnIsbnBook.isEmpty() || !isOwnIsbnBook.get().getBookIsbn().equals(item.getIsbn()) ) {

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
                    bookRepository.save(saveBook);
                }
            }
        }

        String ownTitleAfterApi  = split(paramMap.get("query"));
        List<Book>  ownBookAfterSaveAfterApi=bookRepository.findBooksBySplitTitleContaining(ownTitleAfterApi);

        if(ownBookAfterSaveAfterApi!=null&& !ownBookAfterSaveAfterApi.isEmpty()) {
            OwnSearchBook(showBooks, ownBookAfterSaveAfterApi);
            log.info("AfterApi::::");
        }

        return showBooks;
    }

    private void OwnSearchBook(List<BookVO> showBooks, List<Book> ownBookAfterSaveAfterApi) {
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

    @GetMapping("/r1/userBook/{userId}")
    public List<UserBookReadVO> getUserBook(@PathVariable long userId) throws JsonProcessingException, MalformedURLException {

        RestTemplate restTemplate = new RestTemplate();
        List<UserBookReadVO> userBookReadVOList = new ArrayList<>();
        // 내 책장에서 데이터 가져오기
        User user = userRepository.findUserById(userId);
        List<UserBook> userBooks =userBookRepository.findUserBooksByUserId(userId);

        //api 쏴서 책 장수 업데이트
        String urlString = "http://www.aladin.co.kr";
        URL url = new URL(urlString);
        for (UserBook userBook : userBooks) {
            Optional<Book> book =bookRepository.findBookByBookIsbn(userBook.getBookIsbn());

            UriComponents uriComponents = UriComponentsBuilder.newInstance()
                    .scheme(url.getProtocol())
                    .host(url.getHost())
                    .path("/ttb/api/ItemSearch.aspx")
                    .queryParam("ttbkey", libraryKey)
                    .queryParam("Query",book.get().getPublisher())
                    .queryParam("QueryType","Publisher")
                    .queryParam("start",1)
                    .queryParam("MaxResults",10)
                    .queryParam("output","JS")
                    .queryParam("SearchTarget","Book")
                    .build();
            String uriLibrary = uriComponents.toUriString();
            System.err.println(uriLibrary);

            Result response = restTemplate.getForObject(uriLibrary,Result.class);

            log.error("uriLibrary::::{}",response);


            if(response!=null&&response.getDocs() != null) {

                for (Doc doc  :response.getDocs()) {

                    if(doc.getPage()!=null) {
                        book.ifPresent(value -> {
                                    value.setPageCount(doc.getPage());
                                    value.setCategory(doc.getSubject());
                                    bookRepository.save(value);
                                }
                        );
                    }
                }
            }
        }

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
                userBookReadVOList.add(userBookReadVO);
            }
        }

        return new ArrayList<>(userBookReadVOList);

    }

    @PostMapping("/c1/userBook")
    public void insertUserBookBy( @RequestBody UserBookSaveVO request)  {

        System.err.println(request);

        Optional<User> user = userRepository.findById(request.getUserId());

        if(user.isPresent()) {
            Optional<Book> book =bookRepository.findBookByBookIsbn(request.getBookIsbn());
            log.error("장바구니 북 :::{}",book.isPresent());
            boolean isOwnBook=userBookRepository.existsByBook(book.get());

            if(!isOwnBook) {
                UserBook userBook = new UserBook();
                userBook.setBook(book.get());
                userBook.setUser(user.get());
                userBook.setBookIsbn(book.get().getBookIsbn());
                userBookRepository.save(userBook);
            }
        }
    }
}
