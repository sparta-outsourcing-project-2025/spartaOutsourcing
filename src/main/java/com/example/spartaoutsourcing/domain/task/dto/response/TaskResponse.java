package com.example.spartaoutsourcing.domain.task.dto.response;

import java.time.LocalDateTime;

import com.example.spartaoutsourcing.domain.task.enums.TaskPriority;
import com.example.spartaoutsourcing.domain.task.enums.TaskStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskResponse {
	private Long id;
	private String title;
	private String description;
	private LocalDateTime dueDate;
	private TaskPriority priority;
	private TaskStatus status;
	private Long assigneeId;
	private Assignee assignee;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static TaskResponse of(Long id, String title, String description, LocalDateTime dueDate, TaskPriority priority,
		TaskStatus status, Long assigneeId, Assignee assigneeResponse, LocalDateTime createdAt, LocalDateTime updatedAt) {
		return new TaskResponse(id, title, description, dueDate, priority, status, assigneeId, assigneeResponse, createdAt, updatedAt);
	}
}