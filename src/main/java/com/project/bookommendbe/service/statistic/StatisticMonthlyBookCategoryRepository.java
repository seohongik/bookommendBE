package com.project.bookommendbe.service.statistic;

import com.project.bookommendbe.dto.MonthlyBookCategory;
import com.project.bookommendbe.entity.ReadingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticMonthlyBookCategoryRepository extends JpaRepository<ReadingRecord, Long> {
    @Query(value = """

            SELECT  COUNT(b.book_category) AS categoryCount,
                      b.book_category AS category
                FROM (
                    SELECT 1 AS month UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
                    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8
                    UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12
                ) m
                LEFT JOIN reading_record r ON MONTH(r.date) = m.month AND YEAR(r.date) = :yearNow
                LEFT JOIN user_book ub ON ub.book_isbn = r.book_isbn
                INNER JOIN book b ON b.book_isbn = r.book_isbn
                WHERE ub.user_id = :userId
                GROUP BY b.book_category
                ORDER BY m.month
        """, nativeQuery = true)
    List<MonthlyBookCategory> findMonthlyBookMoneyByUserIdAndYear(@Param("userId") Long userId,
                                                                  @Param("yearNow") int yearNow);
}