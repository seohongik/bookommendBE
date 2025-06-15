package com.project.bookommendbe.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

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

    private LocalDate startedAt;
    private LocalDate finishedAt;
}