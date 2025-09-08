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
    DELETE_USER("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),

    // 기본적으로 만든 내용입니다 나중에 커스텀 합시다~~
    NOT_FOUND("데이터가 없습니다.", HttpStatus.NOT_FOUND),
    FORBIDDEN("권한이 없습니다.", HttpStatus.FORBIDDEN),

    // user
    USER_NOT_FOUND("유저를 찾을 수 없습니다", HttpStatus.NOT_FOUND),

    // annotation
    AUTH_ANNOTATION_MISMATCH_TYPE("@Auth와 AuthUser 타입은 함께 사용되어야 합니다",HttpStatus.INTERNAL_SERVER_ERROR),

    // task
    TASK_NOT_FOUND("해당 ID의 작업을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_TASK_STATUS("유효하지 않은 상태값입니다.", HttpStatus.BAD_REQUEST),
    ASSIGNEE_NOT_FOUND("해당 작업의 담당자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // team
    TEAM_NAME_DUPLICATED("팀 이름이 이미 존재합니다",HttpStatus.BAD_REQUEST),
    MEMBER_ALREADY_EXISTS("사용자가 이미 팀 멤버입니다", HttpStatus.BAD_REQUEST),
    TEAM_NOT_FOUND("삭제되었거나 존재하지 않는 팀입니다.", HttpStatus.BAD_REQUEST),

    // member
    USER_ALREADY_TEAM_MEMBER("사용자가 이미 팀 멤버입니다",  HttpStatus.BAD_REQUEST),

    // dashboard
    DASHBOARD_TEAM_NOT_FOUND("팀을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // comment
    COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND ),

    // activity
    INVALID_REQUEST_PARAMETER("잘못된 요청 파라미터입니다", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;
}
