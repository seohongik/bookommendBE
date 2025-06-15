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

    private LocalDate date;         // 읽은 날짜
    private Integer pagesRead;      // 읽은 페이지 수
    private Integer fromPage;       // 시작 페이지 (optional)
    private Integer toPage;         // 마지막 페이지 (optional)

    private Integer percent;
    private String memo;            // 간단한 메모 (optional)

    private String location;
    private String withWho;
    private String reason;
    private String comment;

}