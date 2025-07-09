package com.project.bookommendbe.service.recordAndReview.review;

import com.project.bookommendbe.entity.Review;
import com.project.bookommendbe.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public abstract class ReviewServiceSuper {

    protected final ReviewRepository reviewRepository;

    public ReviewServiceSuper(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public abstract List<Review> findReviewsByUserAndReviewDateOpen(Optional<User> user, String date);
}
