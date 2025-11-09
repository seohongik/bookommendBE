package com.project.bookommendbe.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName="id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName="id")
    private UserBook userBook;

    private  String reportTitle;

    private  String reportContent;

    private  String sympathyLines;

    private  LocalDate reportDate;

    private String year;

    private String month;

    private LocalDateTime createdAt;

    private String createdBy;


}
