package com.project.bookommendbe.dto;

import lombok.Data;

@Data
public class RecordVO{
    private String date;         // 읽은 날짜
    private int readAmountCount;      // 읽은 페이지 수
    private int fromPage;       // 시작 페이지 (optional)
    private int toPage;         // 마지막 페이지 (optional)
    private int percent;
    private String opinion;
    private String comment;
    private String time;
    private String bookIsbn;
}
