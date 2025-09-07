package com.example.spartaoutsourcing.domain.comment.dto.response;

import com.example.spartaoutsourcing.domain.user.dto.UserCommentResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponse {
    private final Long id;
    private final String content;
    private final Long taskId;
    private final UserCommentResponse user;
    private final Long parentId;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;


    public static CommentResponse of(Long id, String content, Long taskId, UserCommentResponse user, Long parentId, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return new CommentResponse(id, content, taskId, user, parentId, createdAt, modifiedAt);
    }

}
