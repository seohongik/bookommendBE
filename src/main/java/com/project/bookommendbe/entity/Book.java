package com.project.bookommendbe.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "book_isbn" }) })
@ToString(exclude = {"id"})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;          // 책 제목
    private String author;         // 저자
    private String publisher;      // 출판사
    private String bookIsbn;           // ISBN 번호

    private String image;

    private String publishedDate; // 출판일

    @Column(length = 10000)
    private String  description;    // 책 설명
    private String category;       // 카테고리/장르
    private String pageCount;     // 총 페이지 수
    private String language;       // 언어

    private String coverImageUrl;  // 책 표지 이미지 링크

    @Enumerated(EnumType.STRING)
    private BookCategory bookCategory;


    @Enumerated(EnumType.STRING)
    private BookType bookType;

    private String discount;

    private String splitTitle;

    @OneToMany(mappedBy = "book")
    private List<UserBook> userBooks;

//    @ManyToOne
//    @JoinColumn(name = "user_book_id")
//    private UserBook userBook;






}
