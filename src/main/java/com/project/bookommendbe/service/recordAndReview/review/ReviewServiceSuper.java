package com.project.bookommendbe.service.recordAndReview.review;

import com.project.bookommendbe.entity.Review;
import com.project.bookommendbe.entity.User;

import java.util.List;
import java.util.Optional;

public abstract class ReviewServiceSuper {

    protected ReviewRepository reviewRepository;

    public ReviewServiceSuper(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public abstract List<Review> findReviewsByUserAndReviewDateOpen(Optional<User> user, String date);
}
