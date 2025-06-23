package com.project.bookommendbe.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Data
public class UserFindVO {


    private String signUpId;

    @Pattern(regexp = "^010-?\\d{3,4}-?\\d{4}$", message = "휴대폰 번호 형식이 아닙니다.")
    private String phoneNumber;

    private String email;

    private String item;
}


