package com.example.spartaoutsourcing.domain.task.dto.response;

import java.time.Instant;
import java.time.LocalDateTime;

import com.example.spartaoutsourcing.domain.task.enums.TaskPriority;
import com.example.spartaoutsourcing.domain.task.enums.TaskStatus;

import lombok.Getter;

@Getter
public class TaskResponse {
	private Long id;
	private String title;
	private String description;
	private LocalDateTime dueDate;
	private TaskPriority priority;
	private TaskStatus status;
	private Long assigneeId;
	private Assignee assigneeResponse;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public TaskResponse(Long id, String title, String description, LocalDateTime dueDate, TaskPriority priority, TaskStatus status,
		Long assigneeId, Assignee assigneeResponse, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.dueDate = dueDate;
		this.priority = priority;
		this.status = status;
		this.assigneeId = assigneeId;
		this.assigneeResponse = assigneeResponse;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public static TaskResponse of(Long id, String title, String description, LocalDateTime dueDate, TaskPriority priority,
		TaskStatus status, Long assigneeId, Assignee assigneeResponse, LocalDateTime createdAt, LocalDateTime updatedAt) {
		return new TaskResponse(id, title, description, dueDate, priority, status, assigneeId, assigneeResponse, createdAt, updatedAt);
	}
}