package com.example.spartaoutsourcing.domain.comment.dto.response;

import com.example.spartaoutsourcing.domain.user.dto.UserCommentResponse;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentSaveResponse {
    private final Long id;
    private final String content;
    private final Long taskId;
    private final UserCommentResponse user;
    private final Long parentId;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    private CommentSaveResponse(Long id, String content, Long taskId, UserCommentResponse user, Long parentId, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.content = content;
        this.taskId = taskId;
        this.user = user;
        this.parentId = parentId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static CommentSaveResponse of(Long id, String content, Long taskId, UserCommentResponse user, Long parentId, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new CommentSaveResponse(id, content, taskId, user, parentId, createdAt, modifiedAt);
    }
}
