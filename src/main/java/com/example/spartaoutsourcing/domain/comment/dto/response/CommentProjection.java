package com.example.spartaoutsourcing.domain.comment.dto.response;

import java.time.LocalDateTime;

public interface CommentProjection {

    public Long getId();

    public String getContent();

    public Long getTaskId();

    public Long getUserId();

    public String getUserName();

    public String getName();

    public String getEmail();

    public String getRole();

    public LocalDateTime getCreatedAt();

    public LocalDateTime getUpdatedAt();
}
