package com.project.bookommendbe.service.userbook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.bookommendbe.dto.*;
import com.project.bookommendbe.entity.*;
import com.project.bookommendbe.service.book.BookService;
import com.project.bookommendbe.service.recordAndReview.record.RecordService;
import com.project.bookommendbe.service.recordAndReview.review.ReviewService;
import com.project.bookommendbe.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
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

    private final UserService userService;
    private final BookService bookService;
    private final UserBookService userBookService;
    private final ReviewService reviewService;
    private final RecordService recordService;

    @Autowired
    public UserBookController(UserService userService, BookService bookService, RecordService recordService, UserBookService userBookService, ReviewService reviewService) {
        this.userService = userService;
        this.bookService = bookService;
        this.recordService = recordService;
        this.userBookService = userBookService;
        this.reviewService = reviewService;
    }


    @GetMapping("/r1/userBooks/{userId}")
    public List<UserBookVO> getUserBookById(@PathVariable long userId) throws JsonProcessingException, MalformedURLException {
        Optional<User> user =  userService.getUserByIdOpen(userId);
        List<UserBook> userBooks   = userBookService.findUserBooksByUser(user.get());
        return userBookService.getReadingUserBookList(userBooks);
    }

    @GetMapping("/p1/saveUserBook")
    public void insertUserBookBy( @ModelAttribute("request") UserBookSaveVO request) {
        Optional<User> user = userService.getUserByIdOpen(request.getUserId());
        Optional<Book >book = bookService.findBookByBookIsbnOpen(request.getBookIsbn());
        userBookService.saveMyBook(book, user);
    }


    @PutMapping("/u1/userBookPageCount/{userId}/{userBookId}")
    public void updateUserBookPageCountBy(@PathVariable Long userId, @PathVariable Long userBookId, @RequestBody Map<String,String> request) {
        Optional<User> user=userService.getUserByIdOpen(userId);
        userBookService.updateMyBookPageCount(user.get(),userBookId,request);
    }

}
