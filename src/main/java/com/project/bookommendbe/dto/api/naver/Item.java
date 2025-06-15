package com.project.bookommendbe.dto.api.naver;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Item {
    @JsonProperty
    private String title;       // 책 제목
    @JsonProperty
    private String link;        // 네이버 도서 URL
    @JsonProperty
    private String image;       // 썸네일 이미지 URL
    @JsonProperty
    private String author;      // 저자
    @JsonProperty
    private int discount;   // 판매 가격 (nullable)
    @JsonProperty
    private String publisher;   // 출판사
    @JsonProperty
    private String isbn;        // ISBN (문자열로 처리 권장)
    @JsonProperty
    private String description; // 책 소개
    @JsonProperty
    private String  pubdate; // 출간일
}
