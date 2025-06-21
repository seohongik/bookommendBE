package com.project.bookommendbe.dto;


import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Data
public class UserVO {

    private Long id;

    @NotEmpty(message = "이름을 입력해주세요")
    @Size(min = 2, max = 20)
    private String username;

    @Size(min = 5, max = 20)
    @NotEmpty(message="아이디를 입력해주세요")
    private String signUpId;

    @Email(message = "이메일을 입력해주세요")
    private String email;

    @NotEmpty(message = "생년월일 입력해주세요")
    private String dateOfBirth;


    private String gender;

    @Pattern(regexp = "^010-?\\d{3,4}-?\\d{4}$", message = "휴대폰 번호 형식이 아닙니다.")
    private String phoneNumber;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*()-+=]).{8,}$", message = "대문자,숫자,문자,특수문자 최소 8자 이상이어야 합니다")
    private String password;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*()-+=]).{8,}$", message = "대문자,숫자,문자,특수문자 최소 8자 이상이어야 합니다")
    private String confirmPassword;

}