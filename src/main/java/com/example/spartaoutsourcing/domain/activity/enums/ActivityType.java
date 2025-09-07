package com.example.spartaoutsourcing.domain.activity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityType {
    TASK_CREATED("작업 생성", "새로운 작업 '{1}' 를 생성했습니다."),
    TASK_UPDATED("작업 수정","작업 정보를 수정했습니다."),
    TASK_DELETED("작업 삭제", "작업을 삭제했습니다."),
    TASK_STATUS_CHANGED("작업 상태 변경", "작업 상태를 {1}에서 {2}로 변경했습니다."),
    COMMENT_CREATED("댓글 작성", "작업에 댓글을 작성했습니다."),
    COMMENT_UPDATED("댓글 수정", "작업에 댓글을 작성했습니다."),
    COMMENT_DELETED("댓글 삭제", "작업에 댓글을 삭제했습니다.");

    private final String name;
    private final String description;
}
