package com.example.spartaoutsourcing.domain.task.dto.response;

import java.time.LocalDateTime;

import com.example.spartaoutsourcing.domain.task.enums.TaskPriority;
import com.example.spartaoutsourcing.domain.task.enums.TaskStatus;

public interface TaskProjection {

	Long getId();

	String getTitle();

	String getDescription();

	LocalDateTime getDueDate();

	TaskPriority getPriority();

	TaskStatus getStatus();

	Long getAssigneeId();

	String getUsername();

	String getName();

	String getEmail();

	LocalDateTime getCreatedAt();

	LocalDateTime getUpdatedAt();
}
