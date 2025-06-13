package com.project.bookommendbe.dto;


import java.time.LocalDate;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Data
public class UserVO {

    private Long id;
    private String username;
    private String email;
    private String fullName;
    private LocalDate dateOfBirth;
}