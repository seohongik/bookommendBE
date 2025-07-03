package com.project.bookommendbe.service.review;


import com.project.bookommendbe.entity.ReadingRecord;
import com.project.bookommendbe.entity.Review;
import com.project.bookommendbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findReviewsByUserAndReviewDate(User user, String date);
}
