package com.example.spartaoutsourcing.common.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum SuccessCode {
    CREATED(200, "회원가입이 완료되었습니다."),
    OK(200, "요청 성공"),
    SUCCESS_GET_USER(200, "사용자 정보를 조회했습니다."),
    TASK_CREATED(201, "Task가 생성되었습니다."),
    SUCCESS_SEARCH(200,"검색 완료");

    private final int status;
    private final String message;
}
