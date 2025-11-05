package com.project.bookommendbe.service.report;

import com.project.bookommendbe.dto.ReportVO;
import com.project.bookommendbe.dto.UserVO;
import com.project.bookommendbe.entity.Report;
import com.project.bookommendbe.entity.User;
import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@Slf4j
public class ReportService extends ReportServiceSuper {

    protected final ReportRepository reportRepository;

    @Autowired
    ReportService(ReportRepository reportRepository) {
        super(reportRepository);
        this.reportRepository = reportRepository;
    }

    private Report saveUserReportBy(ReportVO saveRequest) {
        //TODO 로직 작성

        return  new Report();
    }

}
