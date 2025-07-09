package com.project.bookommendbe.dto;

import com.project.bookommendbe.entity.BookCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RecommendBookVO {

    private Long id;
    private String title;          // 책 제목
    private String author;         // 저자
    private String publisher;      // 출판사
    private String bookIsbn;           // ISBN 번호
    private String image;
    private String publishedDate; // 출판일
    private String  description;    // 책 설명
    private String  bookCategory;
    private String discount;
}
