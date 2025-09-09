package com.example.spartaoutsourcing.domain.task.dto.request;

import java.time.LocalDateTime;

import com.example.spartaoutsourcing.domain.task.enums.TaskPriority;
import com.example.spartaoutsourcing.domain.task.enums.TaskStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskUpdateRequest {

	private String title;

	private String description;

	private LocalDateTime dueDate;

	// private TaskPriority taskPriority;

	// private TaskStatus taskStatus;

	private Long assigneeId;
}
