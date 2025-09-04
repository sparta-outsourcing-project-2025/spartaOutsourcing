package com.example.spartaoutsourcing.domain.comment.service;

import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.consts.SuccessCode;
import com.example.spartaoutsourcing.common.dto.AuthUserRequest;
import com.example.spartaoutsourcing.common.dto.GlobalApiResponse;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.comment.dto.request.CommentSaveRequest;
import com.example.spartaoutsourcing.domain.comment.dto.response.CommentSaveResponse;
import com.example.spartaoutsourcing.domain.comment.entity.Comment;
import com.example.spartaoutsourcing.domain.comment.repository.CommentRepository;
import com.example.spartaoutsourcing.domain.task.entity.Task;
import com.example.spartaoutsourcing.domain.task.repository.TaskRepository;
import com.example.spartaoutsourcing.domain.user.dto.UserCommentResponse;
import com.example.spartaoutsourcing.domain.user.entity.User;
import com.example.spartaoutsourcing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public GlobalApiResponse<CommentSaveResponse> save(AuthUserRequest authUserRequest, long taskId, CommentSaveRequest request) {
        User user = userRepository.findById(authUserRequest.getId()).orElseThrow(
                () -> new GlobalException(ErrorCode.USER_NOT_FOUND)
        );
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new GlobalException(ErrorCode.TASK_NOT_FOUND)
        );
        Comment parentComment = null;
        if (request.getParentId() != null) {
            parentComment = commentRepository.findById(request.getParentId()).orElseThrow(
                    () -> new GlobalException(ErrorCode.NOT_FOUND)
            );
        }

        Comment comment = Comment.of(user, task, request.getContent(), parentComment);

        Comment saveComment = commentRepository.save(comment);

        return GlobalApiResponse.of(SuccessCode.CREATED, CommentSaveResponse.of(
                saveComment.getId(),
                saveComment.getContent(),
                taskId,
                UserCommentResponse.of(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.getRole().toString()),
                request.getParentId(),
                saveComment.getCreatedAt(),
                saveComment.getModifiedAt()
        ));
    }
}
