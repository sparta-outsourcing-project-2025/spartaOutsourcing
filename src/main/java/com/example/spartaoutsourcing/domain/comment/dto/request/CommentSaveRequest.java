package com.example.spartaoutsourcing.domain.comment.dto.request;

import lombok.Getter;

@Getter
public class CommentSaveRequest {

    @NotBlank
    private String content;

    private Long parentId;
}
