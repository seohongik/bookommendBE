package com.project.bookommendbe.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.bookommendbe.account.Book;
import com.project.bookommendbe.account.User;
import com.project.bookommendbe.account.UserBook;
import com.project.bookommendbe.db.BookRepository;
import com.project.bookommendbe.db.UserBookRepository;
import com.project.bookommendbe.db.UserRepository;
import com.project.bookommendbe.dto.BookVO;
import com.project.bookommendbe.dto.ReviewVO;
import com.project.bookommendbe.dto.api.Channel;
import com.project.bookommendbe.dto.api.Item;
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

        log.error("ownBookAfterSave:{}",ownBookAfterSave);

        if(ownBookAfterSave!=null && !ownBookAfterSave.isEmpty()) {
            OwnSearchBook(showBooks, ownBookAfterSave);
            log.error("beforeApi::::{}",showBooks);
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
                .queryParam("display",100)
                .build();
        String uri = uriComponents.toUriString();

        HttpEntity<Channel> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Channel> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, Channel.class);

        log.info("response:{}",response);
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
            log.error("AfterApi::::{}",showBooks);
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

    @GetMapping("/r1/bookTittle/{userId}")
    public List<UserBook> getUserBook(@PathVariable Long userId) throws JsonProcessingException {

        // 내 책장에서 데이터 가져오기
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            UserBook userBook = userBookRepository.findByUser(user.get());

            List<UserBook> userBookList = new ArrayList<>();
            userBookList.add(userBook);

            return new ArrayList<>(userBookList);
        } else {

            return new ArrayList<>();
        }
    }

    @PostMapping("/c1/review")
    public void insertTransactionBy( @RequestBody ReviewVO request)  {

        System.err.println(request);
        //UserDTO foundUserById=transactionService.findUserBy(reqDTO.getUserId());

        //if(foundUserById.getId() == reqDTO.getUserId()) {
        //}

    }

}
