package com.project.bookommendbe.dto;

import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Data
public class ReadingRecordVO {

    private Long id;
    private Long userId;
    private String username;

    private Long bookId;
    private String bookTitle;

    private LocalDate date;
    private Integer pagesRead;
    private Integer fromPage;
    private Integer toPage;


    private Integer percent;

    private String memo;
}