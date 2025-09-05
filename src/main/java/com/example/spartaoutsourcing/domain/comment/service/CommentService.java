package com.example.spartaoutsourcing.domain.comment.service;

import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.dto.AuthUserRequest;
import com.example.spartaoutsourcing.common.dto.PageResponseDto;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.comment.dto.request.CommentSaveRequest;
import com.example.spartaoutsourcing.domain.comment.dto.response.CommentResponse;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public CommentResponse save(AuthUserRequest authUserRequest, long taskId, CommentSaveRequest request) {
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

        return CommentResponse.of(
                saveComment.getId(),
                saveComment.getContent(),
                taskId,
                UserCommentResponse.of(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.getRole().toString()),
                request.getParentId(),
                saveComment.getCreatedAt(),
                saveComment.getModifiedAt()
        );
    }

    public PageResponseDto<CommentResponse> getComments(Long taskId, Long page, Long size, String sort) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new GlobalException(ErrorCode.TASK_NOT_FOUND)
        );
        long offset = (page-1) * size;
        List<Comment> rootComments = sort.equalsIgnoreCase("oldest")
                ? commentRepository.findAllByTaskIdOrderByAsc(taskId, size, offset) : commentRepository.findAllByTaskIdOrderByDesc(taskId, size, offset);

        List<CommentResponse> comments = new ArrayList<>();
        for (Comment root : rootComments) {
            comments.add(CommentResponse.of(
                    root.getId(),
                    root.getContent(),
                    taskId,
                    UserCommentResponse.of(
                            root.getUser().getId(),
                            root.getUser().getUsername(),
                            root.getUser().getName(),
                            root.getUser().getEmail(),
                            root.getUser().getRole().toString()
                    ),
                    null,
                    root.getCreatedAt(),
                    root.getModifiedAt()
            ));
            comments.addAll(commentRepository.findAllByParentIdOrderByCreatedAtAsc(root.getId())
                    .stream()
                    .map(c -> CommentResponse.of(
                            c.getId(),
                            c.getContent(),
                            taskId,
                            UserCommentResponse.of(
                                    c.getUser().getId(),
                                    c.getUser().getUsername(),
                                    c.getUser().getName(),
                                    c.getUser().getEmail(),
                                    c.getUser().getRole().toString()
                            ),
                            root.getId(),
                            c.getCreatedAt(),
                            c.getModifiedAt()
                    ))
                    .toList()
            );
        }
        Long totalElements = commentRepository.countAllByTaskId(taskId);
        int totalPage= (int)Math.ceil((double)totalElements / size);
        return PageResponseDto.of(comments, totalElements, totalPage, size, page);
    }
}
