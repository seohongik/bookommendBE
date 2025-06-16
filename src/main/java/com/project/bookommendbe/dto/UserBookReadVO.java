package com.project.bookommendbe.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Data
public class UserBookReadVO {

    private String coverImageUrl;
    private long userId;
    private long userBookId;
    private String title;          // 책 제목
    private String author;         // 저자
    private String publisher;      // 출판사
    private String bookIsbn;           // ISBN 번호
    private String publishedDate;
    private String description;
    private long bookId;
    private int pageCount;
    private int pageAmountCount;
}
