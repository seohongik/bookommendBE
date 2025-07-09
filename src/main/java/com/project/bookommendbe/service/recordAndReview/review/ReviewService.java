package com.project.bookommendbe.service.recordAndReview.review;

import com.project.bookommendbe.dto.RecordAndReviewSaveVO;
import com.project.bookommendbe.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class  ReviewService extends ReviewServiceSuper {

    protected ReviewRepository reviewRepository;

    @Autowired
    ReviewService(ReviewRepository reviewRepository) {
        super(reviewRepository);
        this.reviewRepository = reviewRepository;
    }

    // 처리할 서비스 로직 [S]

     public void saveMyReview(Optional<UserBook> userBook, RecordAndReviewSaveVO saveRequest ) {
        Review review = new Review();
        review.setCreatedAt(LocalDateTime.now());
        review.setReviewDate(saveRequest.getRecord().getDate());
        review.setRating(RatingEnum.fromValue(saveRequest.getRating()));
        review.setUser(userBook.get().getUser());
        review.setBook(userBook.get().getBook());
        reviewRepository.save(review);
    }

    @Override
    public List<Review> findReviewsByUserAndReviewDateOpen(Optional<User> user, String date) {
        return reviewRepository.findReviewsByUserAndReviewDate(user.get(), date);
    }


}



