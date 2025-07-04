package com.project.bookommendbe.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class UserFindPwVO {


    @Pattern(regexp = "^010-?\\d{3,4}-?\\d{4}$", message = "휴대폰 번호 형식이 아닙니다.")
    private String phoneNumber;

    @Email(message = "이메일을 입력해주세요")
    private String email;


}


