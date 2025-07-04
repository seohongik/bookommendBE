package com.project.bookommendbe.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginVO {

    @Email(message = "이메일을 입력해주세요")
    @NotEmpty(message = "이메일 값을 입력해주세요")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*()-+=]).{8,}$", message = "비밀 번호는 대문자,숫자,문자,특수문자 최소 8자 이상이어야 합니다")
    private String password;
}
