package com.project.bookommendbe.service.report;

import com.project.bookommendbe.entity.Report;
import com.project.bookommendbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface ReportRepository extends JpaRepository<Report, Long> {



}
