package com.example.spartaoutsourcing.common.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum SuccessCode {

    // COMMON
    OK(200, "요청이 성공적으로 처리되었습니다."),

    // USER
    CREATED(200, "회원가입이 완료되었습니다."),
    SUCCESS_GET_USER(200, "사용자 정보를 조회했습니다."),
    SUCCESS_LOGIN(200, "로그인이 완료되었습니다."),
    SUCCESS_DELETE_USER(200, "회원탈퇴가 완료되었습니다."),

    // TASK
    TASK_FIND(200, "Task를 조회했습니다."),
    TASK_UPDATED(200, "Task가 수정되었습니다."),
    TASK_CREATED(201, "Task가 생성되었습니다."),
    TASK_FIND_ALL(200, "Task 목록을 조회했습니다."),
    TASK_STATUS_UPDATED(200, "작업 상태가 업데이트되었습니다."),
    TASK_DELETED(200, "Task가 삭제되었습니다."),

    // TEAM
    SUCCESS_GET_TEAM(200,"팀 목록을 조회했습니다."),
    TEAM_CREATED(201, "팀이 성공적으로 생성되었습니다."),
    TEAM_DELETED(200, "팀이 성공적으로 삭제되었습니다."),
    TEAM_UPDATE(200, "팀 정보가 성공적으로 업데이트되었습니다."),
    TEAM_FOUND(200, "팀 정보를 조회했습니다."),
    TEAM_MEMBERS_RETRIEVED(200, "팀 멤버 목록을 조회했습니다."),

    // MEMBER
    MEMBER_ADDED(200, "멤버가 성공적으로 추가되었습니다."),
    MEMBER_REMOVE(200, "멤버가 성공적으로 제거되었습니다."),


    // COMMENT
    COMMENT_FIND(200,"댓글을 조회했습니다."),
    COMMENT_UPDATED(200, "댓글이 수정되었습니다."),
    COMMENT_CREATED(201, "댓글이 생성되었습니다."),

    // SEARCH
    SUCCESS_SEARCH(200,"검색 완료"),
    SUCCESS_SEARCH_TASK(200,"작업 검색 완료"),

    //DASHBOARD
    SUCCESS_FIND_DASHBOARD_STATS(200,"대시보드 통계 조회 완료"),
    SUCCESS_DASHBOARD_MY_TASKS(200, "내 작업 요약 조회 완료"),
    SUCCESS_DASHBOARD_TEAM_PROGRESS(200, "팀 진행률 조회 완료"),
    SUCCESS_DASHBOARD_ACTIVITIES(200, "활동 내역 조회 완료");

    private final int status;
    private final String message;
}
