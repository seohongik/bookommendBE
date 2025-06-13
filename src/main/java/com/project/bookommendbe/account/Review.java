package com.project.bookommendbe.account;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode
@ToString
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Book book;

    private String content;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private RatingEnum rating;
}
