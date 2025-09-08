package com.example.spartaoutsourcing.domain.comment.service;

import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.dto.AuthUserRequest;
import com.example.spartaoutsourcing.common.dto.PageResponseDto;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.comment.dto.request.CommentSaveRequest;
import com.example.spartaoutsourcing.domain.comment.dto.request.CommentUpdateRequest;
import com.example.spartaoutsourcing.domain.comment.dto.response.CommentResponse;
import com.example.spartaoutsourcing.domain.comment.entity.Comment;
import com.example.spartaoutsourcing.domain.comment.repository.CommentRepository;
import com.example.spartaoutsourcing.domain.task.entity.Task;
import com.example.spartaoutsourcing.domain.task.service.TaskService;
import com.example.spartaoutsourcing.domain.user.dto.UserCommentResponse;
import com.example.spartaoutsourcing.domain.user.entity.User;
import com.example.spartaoutsourcing.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final TaskService taskService;

    @Transactional
    public CommentResponse save(AuthUserRequest authUserRequest, long taskId, CommentSaveRequest request) {
        User user = userService.getUserById(authUserRequest.getId());
        Task task = taskService.getTaskById(taskId);
        Comment parentComment = null;
        if (request.getParentId() != null) {
            parentComment = commentRepository.findById(request.getParentId()).orElseThrow(
                    () -> new GlobalException(ErrorCode.COMMENT_NOT_FOUND)
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

    @Transactional(readOnly = true)
    public PageResponseDto<CommentResponse> getComments(Long taskId, Long page, Long size, String sort) {
        taskService.getTaskById(taskId);
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
            comments.addAll(commentRepository.findAllByParentIdAndDeletedAtIsNullOrderByCreatedAtAsc(root.getId())
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
        Long totalElements = commentRepository.countAllByTaskIdAndDeletedAtIsNull(taskId);
        int totalPage= (int)Math.ceil((double)totalElements / size);
        return PageResponseDto.of(comments, totalElements, totalPage, size, page);
    }

    @Transactional
    public CommentResponse updateComment(AuthUserRequest authUserRequest, Long commentId, Long taskId, CommentUpdateRequest request) {
        Task task = taskService.getTesKById(taskId);

        Comment comment = commentRepository.findByIdAndDeletedAtIsNull(commentId).orElseThrow(

                () -> new GlobalException(ErrorCode.COMMENT_NOT_FOUND)
        );
        if (!comment.getUser().getId().equals(authUserRequest.getId())) {
            throw new GlobalException(ErrorCode.FORBIDDEN);
        }
        comment.update(request.getContent());

        return CommentResponse.of(
                comment.getId(),
                comment.getContent(),
                task.getId(),
                UserCommentResponse.of(
                        comment.getUser().getId(),
                        comment.getUser().getUsername(),
                        comment.getUser().getName(),
                        comment.getUser().getEmail(),
                        comment.getUser().getRole().toString()
                ),
                comment.getParent().getId(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }

    @Transactional
    public void deleteComment(AuthUserRequest authUserRequest, Long commentId) {

        Comment comment = commentRepository.findByIdAndDeletedAtIsNull(commentId).orElseThrow(
                () -> new GlobalException(ErrorCode.COMMENT_NOT_FOUND)
        );

        if (!comment.getUser().getId().equals(authUserRequest.getId())) {
            throw new GlobalException(ErrorCode.FORBIDDEN);
        }
        if (comment.getParent() != null) {
            comment.delete();
            return;
        }
        comment.delete();
        List<Comment> comments = commentRepository.findAllByParentIdAndDeletedAtIsNull(comment.getId());
        for (Comment c : comments) {
            c.delete();
        }
    }
}
