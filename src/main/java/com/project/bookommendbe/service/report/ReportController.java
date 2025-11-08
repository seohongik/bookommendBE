package com.project.bookommendbe.service.report;

import com.project.bookommendbe.dto.RecordAndReviewSaveVO;
import com.project.bookommendbe.dto.ReportVO;
import com.project.bookommendbe.entity.Report;
import com.project.bookommendbe.entity.User;
import com.project.bookommendbe.entity.UserBook;
import com.project.bookommendbe.service.recordAndReview.record.RecordService;
import com.project.bookommendbe.service.recordAndReview.review.ReviewService;
import com.project.bookommendbe.service.user.UserService;
import com.project.bookommendbe.service.userbook.UserBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/c1/userReport")
    public void saveUserReportBy(@RequestBody ReportVO saveRequest) {

        //TODO 컨트롤러 만들기

        Report report=reportService.saveUserReportBy(saveRequest);



    }
}
