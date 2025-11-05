package com.project.bookommendbe.service.report;

import com.project.bookommendbe.entity.User;

import java.util.Optional;

public abstract class ReportServiceSuper {

    protected final ReportRepository reportRepository;

    public ReportServiceSuper(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }
}
