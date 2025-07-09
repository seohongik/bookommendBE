package com.project.bookommendbe.service.record;

import com.project.bookommendbe.dto.RecordAndReviewSaveVO;
import com.project.bookommendbe.entity.User;
import com.project.bookommendbe.entity.UserBook;
import com.project.bookommendbe.service.review.ReviewService;
import com.project.bookommendbe.service.user.UserService;
import com.project.bookommendbe.service.userbook.UserBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class RecordController {

    private final UserService userService;
    private final RecordService recordService;
    private final UserBookService userBookService;
    private final ReviewService reviewService;

    @Autowired
    public RecordController(UserService userService, RecordService recordService, UserBookService userBookService, ReviewService reviewService) {
        this.userService = userService;
        this.recordService = recordService;
        this.userBookService = userBookService;
        this.reviewService = reviewService;
    }

    @PostMapping("/c1/userBookRecordAndReview")
    public void saveReadingRecordAndReviewBy(@RequestBody RecordAndReviewSaveVO saveRequest) {

        Optional<User> user = userService.getUserByIdOpen(saveRequest.getUserId());
        Optional<UserBook> userBook=userBookService.saveReadBookPageCountAndStatusOpen(user.get(), saveRequest);
        reviewService.saveMyReview(userBook,saveRequest);
        recordService.saveMyRecord(userBook, saveRequest);
    }
}
