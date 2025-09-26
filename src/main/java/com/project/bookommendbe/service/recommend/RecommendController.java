package com.project.bookommendbe.service.recommend;

import com.project.bookommendbe.dto.RecommendBookVO;
import com.project.bookommendbe.entity.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Set;

@RestController
@Slf4j
public class RecommendController {

    private RecommendService recommendService;
    @Autowired
    public RecommendController(RecommendService recommendService) {
        this.recommendService = recommendService;
    }
    @GetMapping("recommend/r1/genre-based-recommend/{userId}")
    public Set<RecommendBookVO> genreBasedRecommend(@PathVariable String  userId) {
        log.info("recommend genre based recommend ::{}",recommendService.genreBasedRecommend(Long.valueOf(userId)));
        return recommendService.genreBasedRecommend(Long.valueOf(userId));
    }
}
