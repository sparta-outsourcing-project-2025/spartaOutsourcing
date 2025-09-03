package com.example.spartaoutsourcing.common.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum SuccessCode {
    CREATED("회원가입이 완료되었습니다."),
    OK("요청 성공"),
    SUCCESS("로그인이 완료되었습니다.");
    private final String message;
}
