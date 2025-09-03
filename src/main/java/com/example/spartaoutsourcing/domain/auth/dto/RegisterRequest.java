package com.example.spartaoutsourcing.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private static final String PASSWORD_REGEX =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=]).{8,100}$";

    @NotBlank
    @Size(min = 4, max = 20, message = "Username은 4~20자여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username은 영문과 숫자만 가능합니다.")
    private String username;

    @Email(message = "유효한 이메일 주소여야 합니다.")
    private String email;

    @NotBlank
    @Pattern(regexp = PASSWORD_REGEX, message = "password는 최소 8자 이상, 영문 대소문자, 숫자, 특수문자를 모두 포함해야 합니다.")
    private String password;

    @NotBlank
    @Size(min = 2, max = 50, message = "name은 2~50자여야 합니다.")
    private String name;

}
