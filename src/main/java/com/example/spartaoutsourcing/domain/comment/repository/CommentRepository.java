package com.example.spartaoutsourcing.domain.comment.repository;

import com.example.spartaoutsourcing.domain.comment.entity.Comment;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 작업 ID를 기준으로 soft delete 처리 되지 않은 레코드를 검색함.
    @Query(value = """
        SELECT *
        FROM comments c
        LEFT JOIN users u ON u.id = c.user_id
        WHERE c.task_id = :taskId
            AND c.parent_id IS NULL
            AND c.deleted_at IS NULL
        ORDER BY c.created_at DESC
        LIMIT :size OFFSET :offset
    """,  nativeQuery = true)
    List<Comment> findAllByTaskIdOrderByDesc(
            @Param("taskId") Long taskId,
            @Param("size") Long size,
            @Param("offset")  Long offset);

    @Query(value = """
        SELECT *
        FROM comments c
        LEFT JOIN users u ON u.id = c.user_id
        WHERE c.task_id = :taskId
            AND c.parent_id IS NULL
            AND c.deleted_at IS NULL
        ORDER BY c.created_at ASC
        LIMIT :size OFFSET :offset
    """, nativeQuery = true)
    List<Comment> findAllByTaskIdOrderByAsc(
            @Param("taskId") Long taskId,
            @Param("size") Long size,
            @Param("offset")  Long offset);

    @EntityGraph(attributePaths = {"user"})
    List<Comment> findAllByParentIdAndDeletedAtIsNullOrderByCreatedAtAsc(Long parentId);

    Long countAllByTaskIdAndDeletedAtIsNull(Long taskId);

    @EntityGraph(attributePaths = {"user","parent"})
    Optional<Comment> findByIdAndDeletedAtIsNull(@NonNull Long id);

    List<Comment> findAllByParentIdAndDeletedAtIsNull(Long parentId);

}
