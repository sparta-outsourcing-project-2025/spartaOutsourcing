package com.example.spartaoutsourcing.domain.activities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivitiesType {
    TASK_CREATED("작업 생성"),
    TASK_UPDATED("작업 수정"),
    TASK_DELETED("작업 삭제"),
    TASK_STATUS_CHANGED("작업 상태 변경"),
    COMMENT_CREATED("댓글 작성"),
    COMMENT_UPDATED("댓글 수정"),
    COMMENT_DELETED("댓글 삭제");

    private final String description;
}
