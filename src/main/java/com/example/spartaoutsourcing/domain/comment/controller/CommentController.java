package com.example.spartaoutsourcing.domain.comment.controller;

import com.example.spartaoutsourcing.common.annotation.Auth;
import com.example.spartaoutsourcing.common.consts.SuccessCode;
import com.example.spartaoutsourcing.common.dto.AuthUserRequest;
import com.example.spartaoutsourcing.common.dto.GlobalApiResponse;
import com.example.spartaoutsourcing.common.dto.PageResponseDto;
import com.example.spartaoutsourcing.domain.comment.dto.request.CommentSaveRequest;
import com.example.spartaoutsourcing.domain.comment.dto.request.CommentUpdateRequest;
import com.example.spartaoutsourcing.domain.comment.dto.response.CommentResponse;
import com.example.spartaoutsourcing.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/tasks/{taskId}/comments")
    public GlobalApiResponse<CommentResponse> saveComment(
            @Auth AuthUserRequest authUserRequest,
            @PathVariable("taskId") Long taskId,
            @Valid @RequestBody CommentSaveRequest request
    ) {
        return GlobalApiResponse.of(SuccessCode.COMMENT_CREATED, commentService.save(authUserRequest, taskId, request));
    }

    @GetMapping("/api/tasks/{taskId}/comments")
    public GlobalApiResponse<PageResponseDto<CommentResponse>> getComments(
            @PathVariable("taskId") Long taskId,
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(defaultValue = "newest") String sort
    ) {
        return GlobalApiResponse.of(SuccessCode.COMMENT_FIND, commentService.getComments(taskId, page, size, sort));
    }

    @PutMapping("/api/tasks/{taskId}/comments/{commentId}")
    public GlobalApiResponse<CommentResponse> updateComment(
            @Auth AuthUserRequest authUserRequest,
            @PathVariable("commentId") Long commentId,
            @PathVariable("taskId") Long taskId,
            @Valid @RequestBody CommentUpdateRequest request
    ) {
        return GlobalApiResponse.of(SuccessCode.COMMENT_UPDATED, commentService.update(authUserRequest, commentId, taskId, request));
    }

    @DeleteMapping("/api/tasks/{taskId}/comments/{commentId}")
    public GlobalApiResponse<Void> deleteComment(
            @Auth AuthUserRequest authUserRequest,
            @PathVariable("commentId") Long commentId,
            @PathVariable("taskId") Long taskId
    ) {
        commentService.delete(authUserRequest, commentId, taskId);
        return GlobalApiResponse.of(SuccessCode.COMMENT_DELETED,null);
    }
}
