package com.example.spartaoutsourcing.common.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NOT_AUTHENTICATED( "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    EMAIL_DUPLICATED( "이미 존재하는 이메일입니다", HttpStatus.BAD_REQUEST),
    USERNAME_DUPLICATED( "이미 존재하는 사용자명입니다.", HttpStatus.BAD_REQUEST),
    LOGIN_CHECKED("잘못된 사용자명 또는 비밀번호입니다",  HttpStatus.UNAUTHORIZED),

    // 기본적으로 만든 내용입니다 나중에 커스텀 합시다~~
    NOT_FOUND("데이터가 없습니다.", HttpStatus.NOT_FOUND),
    FORBIDDEN("권한이 없습니다.", HttpStatus.FORBIDDEN),

    // 유저
    USER_NOT_FOUND("유저를 찾을 수 없습니다", HttpStatus.NOT_FOUND),

    // 어노테이션
    AUTH_ANNOTATION_MISMATCH_TYPE("@Auth와 AuthUser 타입은 함께 사용되어야 합니다",HttpStatus.INTERNAL_SERVER_ERROR),

    // task
    TASK_NOT_FOUND("작업을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // team
    TEAM_NAME_DUPLICATED("팀 이름이 이미 존재합니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;
}
