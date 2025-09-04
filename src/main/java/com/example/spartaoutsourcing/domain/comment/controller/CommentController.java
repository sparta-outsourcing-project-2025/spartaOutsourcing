package com.example.spartaoutsourcing.domain.comment.controller;

import com.example.spartaoutsourcing.common.annotation.Auth;
import com.example.spartaoutsourcing.common.dto.AuthUserRequest;
import com.example.spartaoutsourcing.common.dto.GlobalApiResponse;
import com.example.spartaoutsourcing.domain.comment.dto.request.CommentSaveRequest;
import com.example.spartaoutsourcing.domain.comment.dto.response.CommentSaveResponse;
import com.example.spartaoutsourcing.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/tasks/{taskId}/comments")
    public GlobalApiResponse<CommentSaveResponse> createComment(
            @Auth AuthUserRequest authUserRequest,
            @PathVariable("taskId") Long taskId,
            @Valid @RequestBody CommentSaveRequest request
    ) {
        return commentService.save(authUserRequest, taskId, request);
    }
}
