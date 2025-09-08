package com.example.spartaoutsourcing.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentUpdateRequest {

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;
}
