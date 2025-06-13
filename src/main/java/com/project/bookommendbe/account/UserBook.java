package com.project.bookommendbe.account;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookIsbn;

    @ManyToOne
    private User user;

    @ManyToOne
    private Book book;

    @Enumerated(EnumType.STRING)
    private ReadingStatus status; // 읽기 상태: TO_READ, READING, COMPLETED

    private LocalDate startedAt;
    private LocalDate finishedAt;
}