package com.project.bookommendbe.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Data
@ToString(exclude = {"id", "book","user"})
public class UserBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookIsbn;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Enumerated(EnumType.STRING)
    private ReadingStatus status; // 읽기 상태: TO_READ, READING, COMPLETED

    private String startedAt;
    private String  finishedAt;

    @ColumnDefault("0")
    private int pageCount;     // 총 페이지 수

    @ColumnDefault("1")
    private int fromPage;     // 개별 페이지 수

    private int pagePrice;

}