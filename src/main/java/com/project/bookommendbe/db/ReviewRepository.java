package com.project.bookommendbe.db;


import com.project.bookommendbe.entity.ReadingRecord;
import com.project.bookommendbe.entity.Review;
import com.project.bookommendbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findReviewsByUserAndReviewDate(User user, String date);
}
