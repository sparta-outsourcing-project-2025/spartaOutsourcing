package com.example.spartaoutsourcing.domain.activity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityType {
    TASK_CREATED("created_task", "새로운 작업 '{1}'을 생성했습니다."),
    TASK_UPDATED("updated_task","작업 정보를 수정했습니다."),
    TASK_DELETED("deleted_task", "작업을 삭제했습니다."),
    TASK_STATUS_CHANGED("status_changed_task", "작업 상태를 {1}에서 {2}로 변경했습니다."),
    COMMENT_CREATED("created_comment", "작업에 댓글을 작성했습니다."),
    COMMENT_UPDATED("updated_comment", "작업에 댓글을 수정했습니다."),
    COMMENT_DELETED("updated_comment", "작업에 댓글을 삭제했습니다.");

    private final String action;
    private final String description;
}
