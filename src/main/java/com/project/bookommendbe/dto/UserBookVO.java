package com.project.bookommendbe.dto;

import lombok.*;
import lombok.experimental.Accessors;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Data
public class UserBookVO {

    private Long id;
    private Long userId;
    private String username;
    private Long bookId;

    private String status; // TO_READ, READING, COMPLETED
    private LocalDate startedAt;
    private LocalDate finishedAt;

}