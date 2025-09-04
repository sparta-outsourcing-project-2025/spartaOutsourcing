package com.example.spartaoutsourcing.domain.comment.repository;

import com.example.spartaoutsourcing.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
