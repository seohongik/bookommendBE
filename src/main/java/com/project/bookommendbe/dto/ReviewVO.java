package com.project.bookommendbe.dto;

import com.project.bookommendMC.entity.account.RatingEnum;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Data
public class ReviewVO {

    private Long id;
    private Long userId;
    private String username;
    private Long bookId;
    private String bookTitle;

    private String content;
    private LocalDateTime createdAt;
    private RatingEnum rating;
}