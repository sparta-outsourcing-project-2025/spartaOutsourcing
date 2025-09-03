package com.example.spartaoutsourcing.common.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NOT_AUTHENTICATED( "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    EMAIL_DUPLICATED( "중복되는 이메일입니다.", HttpStatus.BAD_REQUEST),

    // 기본적으로 만든 내용입니다 나중에 커스텀 합시다~~
    NOT_FOUND("데이터가 없습니다.", HttpStatus.NOT_FOUND),
    FORBIDDEN("권한이 없습니다.", HttpStatus.FORBIDDEN);

    private final String message;
    private final HttpStatus httpStatus;
}
