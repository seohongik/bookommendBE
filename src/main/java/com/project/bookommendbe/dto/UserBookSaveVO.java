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
public class UserBookSaveVO {

    private String  bookIsbn;
    private long userId;

}