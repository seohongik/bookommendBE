package com.project.bookommendbe.service.report;

import com.project.bookommendbe.entity.Report;
import com.project.bookommendbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findAllByUserOrderByCreatedAt(User user);

    Set<String> findYearByUserIdAndUserBookId(Long userId, Long UserBookId);
}
