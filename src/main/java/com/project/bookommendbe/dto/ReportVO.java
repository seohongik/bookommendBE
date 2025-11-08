package com.project.bookommendbe.dto;


import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Data
public class ReportVO {

    private Long userId;

    private String year;

    private String month;

    private Long userBookId;

    private String reportTitle;

    private String reportContent;

    private List<String> sympathyLines;

    private LocalDateTime reportDateTime;

    private LocalDateTime createdAt;
}
