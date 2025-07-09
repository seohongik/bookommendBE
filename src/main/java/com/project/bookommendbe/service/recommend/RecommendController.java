package com.project.bookommendbe.service.recommend;

import com.project.bookommendbe.entity.Book;
import com.project.bookommendbe.service.book.BookService;
import com.project.bookommendbe.service.user.UserService;
import com.project.bookommendbe.service.userbook.UserBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class RecommendController {

    private RecommendService recommendService;

    @Autowired
    public RecommendController(RecommendService recommendService) {
        this.recommendService = recommendService;
    }

    @GetMapping("recommend/r1/genre-based-recommend/{userId}")
    public Map<Book, String> genreBasedRecommend(@PathVariable String  userId) {

        return recommendService.genreBasedRecommend(Long.valueOf(userId));
    }
}
