package com.project.bookommendbe.dto;

import com.project.bookommendbe.entity.*;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimelineVO {

    private Long id;
    private String bookIsbn;
    private String date;         // 읽은 날짜
    private int readAmountCount;      // 읽은 페이지 수
    private String fromPage;       // 시작 페이지 (optional)
    private String toPage;         // 마지막 페이지 (optional)
    private int betweenPage; // 전 대비 읽은 페이지
    private String percent;
    private String opinion;
    private String comment;
    private ReadingStatus status;
    private String time;
    private String rating;
    private String title;
    private String author;
}
