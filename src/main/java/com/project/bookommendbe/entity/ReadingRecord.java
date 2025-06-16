package com.project.bookommendbe.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Data
public class ReadingRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookIsbn;

    @ManyToOne
    private User user;

    @ManyToOne
    private UserBook userBook;

    private String date;         // 읽은 날짜
    private int readAmountCount;      // 읽은 페이지 수
    private int fromPage;       // 시작 페이지 (optional)
    private int toPage;         // 마지막 페이지 (optional)

    private int betweenPage; // 전 대비 읽은 페이지

    private double percent;

    private String opinion;
    private String comment;

    private String status;

    private String time;

}