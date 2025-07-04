package com.project.bookommendbe.userbook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.bookommendbe.dto.*;
import com.project.bookommendbe.dto.api.library.Result;
import com.project.bookommendbe.entity.*;
import com.project.bookommendbe.dto.api.naver.Channel;
import com.project.bookommendbe.service.RestTempService;
import com.project.bookommendbe.service.book.BookService;
import com.project.bookommendbe.service.record.RecordService;
import com.project.bookommendbe.service.review.ReviewService;
import com.project.bookommendbe.service.user.UserService;
import com.project.bookommendbe.service.userbook.UserBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.lang.module.Configuration;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/*
* c -> create
* r -> read
* u -> update
* d -> delete
* */

@Slf4j
@RestController
public class UserBookController {

    private final UserService userService;
    private final BookService bookService;
    private final UserBookService userBookService;
    private final ReviewService reviewService;
    private final RecordService recordService;
    private final RestTempService restTempService;


    @Value("${naver.clientId}")
    private String clientId;

    @Value("${naver.secretId}")
    private String secretId;

    @Value("${library.key}")
    private String libraryKey;


    @Autowired
    public UserBookController(UserService userService, BookService bookService, RecordService recordService, UserBookService userBookService, ReviewService reviewService, RestTempService restTempService) {
        this.userService = userService;
        this.bookService = bookService;
        this.recordService = recordService;
        this.userBookService = userBookService;
        this.reviewService = reviewService;
        this.restTempService = restTempService;
    }

    @GetMapping(value = "/r1/searchBookInfo" )
    public List<BookVO> requestUserBook(@RequestParam  MultiValueMap<String, String> paramMap) throws MalformedURLException {

        List<BookVO> showableBooks = new ArrayList<>();
        showableBooks=bookService.findSavedBook(showableBooks, paramMap);
        log.info("!!!!!!!!!!!!!!!!!!!!beforeAPi");


        if (showableBooks.isEmpty()) {

            String url = "https://openapi.naver.com";
            String path  ="/v1/search/book.json";

            Map<String,String> headersItem = new ConcurrentHashMap<>();
            headersItem.put("X-Naver-Client-Id", clientId);
            headersItem.put("X-Naver-Client-Secret", secretId);

            ResponseEntity<Channel> response = restTempService.response(url,path, paramMap, headersItem, HttpMethod.GET,Channel.class);
            bookService.saveApiNAVERBooks(response.getBody().getItems());
            showableBooks = bookService.findSavedBook(showableBooks, paramMap);
            log.info("!!!!!!!!!!!!!!!!!!!!afterAPi");

        }

        return showableBooks;
    }


    @GetMapping("/r1/userBooks/{userId}")
    public List<UserBookVO> getUserBookById(@PathVariable long userId) throws JsonProcessingException, MalformedURLException {

        List<UserBookVO> userBookReads = new ArrayList<>();
        Optional<User> user=userService.findUserById(userId);
        //List<UserBook> userBooks=userBookService.getReadingUserBookList(user.get());
        List<UserBook> userBooks   = userBookService.findUserBooksByUser(user.get());
        userBookReads = bookService.getReadingUserBookList(userBooks);
        return new ArrayList<>(userBookReads);

    }

    @PostMapping("/c1/userBook")
    public void insertUserBookAndBookCategoryBy( @RequestBody UserBookSaveVO request) throws MalformedURLException {

        Optional<User> user = userService.findUserById(request.getUserId());

        if(user.isPresent()) {

            boolean isOwnBook=userBookService.existsUserBookByUserAndBookIsbn(user.get(),request.getBookIsbn());
            MultiValueMap<String,String> paramMap = new LinkedMultiValueMap<>();

            String url = "https://www.nl.go.kr";
            String path  ="/seoji/SearchApi.do";
            paramMap.put("cert_key", Collections.singletonList(libraryKey));
            paramMap.put("result_style", Collections.singletonList("json"));
            paramMap.put("isbn", Collections.singletonList(request.getBookIsbn()));
            paramMap.put("page_no", Collections.singletonList("1"));
            paramMap.put("page_size", Collections.singletonList("10"));


            ResponseEntity<Result> response = restTempService.response(url,path, paramMap, null, HttpMethod.GET, Result.class);

            log.error(response.getBody().toString());
            Optional<Book> book= bookService.getBookByIsbn(request.getBookIsbn());

            if(response.getBody().getDocs()!=null) {
                bookService.saveApiCategoryBooks(response.getBody().getDocs(), book);
            }
            userBookService.saveMyBook(isOwnBook, book, user);

        }
    }
    @PutMapping("/u1/userBookPageCount/{userId}/{userBookId}")
    public void updateUserBookPageCountBy(@PathVariable Long userId, @PathVariable Long userBookId, @RequestBody Map<String,String> request) {
        Optional<User> user=userService.findUserById(userId);
        userBookService.updateMyBookPageCount(user.get(),userBookId,request);
    }

    @PostMapping("/c1/userBookRecordAndReview")
    public void saveReadingRecordAndReviewBy(@RequestBody RecordAndReviewSaveVO saveRequest) {

        Optional<User> user = userService.findUserById(saveRequest.getUserId());
        Optional<UserBook> userBook=userBookService.saveReadBookPageCountAndStatus(user.get(), saveRequest);
        reviewService.saveMyReview(userBook,saveRequest);
        recordService.saveMyRecord(userBook, saveRequest);

    }
}
