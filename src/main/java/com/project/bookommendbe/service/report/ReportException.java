package com.project.bookommendbe.service.report;

public class ReportException extends RuntimeException {
    private final String message;
    public ReportException(String message) {
        super(message);
        this.message = message;
    }
}
