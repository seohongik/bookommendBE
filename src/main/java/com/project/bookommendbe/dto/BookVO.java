package com.project.bookommendbe.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class BookVO {

    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String bookIsbn;
    private String publishedDate;
    private String description;
    private String category;
    private int pageCount;
    private String language;
    private String coverImageUrl;
}
