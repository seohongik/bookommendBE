package com.project.bookommendbe.service.recommend;

import com.project.bookommendbe.entity.Book;
import com.project.bookommendbe.entity.User;
import com.project.bookommendbe.entity.UserBook;
import com.project.bookommendbe.service.book.BookService;
import com.project.bookommendbe.service.user.UserService;
import com.project.bookommendbe.service.user.UserServiceSuper;
import com.project.bookommendbe.service.userbook.UserBookService;
import com.project.bookommendbe.service.userbook.UserBookServiceSuper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RecommendService {

    private UserBookService userBookService;
    private UserService userService;
    private BookService bookService;

    @Autowired
    public RecommendService(UserBookService userBookService, UserService userService, BookService bookService) {
        this.userBookService = userBookService;
        this.userService = userService;
        this.bookService = bookService;
    }


    public Map<Book,String > genreBasedRecommend(Long id) {

        Optional<User> my = userService.getUserByIdOpen(id);

        List<UserBook> myBooks = userBookService.getUserBooksByUserOpen(my);
        List<UserBook> otherBooks = userBookService.findAllOpen();

        Map<Book,String > recommend = new HashMap<>();
        for (UserBook otherBook: otherBooks) {
            for (UserBook myBook : myBooks) {

                if(myBook.getBook().getBookCategory()!=null && otherBook.getBook().getBookCategory()!=null) {

                    if (myBook.getBook().getBookCategory().equals(otherBook.getBook().getBookCategory())) {
                        Optional<Book> book=bookService.findBookByBookIsbnOpen(otherBook.getBookIsbn());
                        if(book.isPresent()) {
                            recommend.put(book.get(), book.get().getImage());
                        }
                    }
                }
            }
        }
        return recommend;
    }
}
