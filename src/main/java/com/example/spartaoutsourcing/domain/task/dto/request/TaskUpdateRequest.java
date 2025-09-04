package com.example.spartaoutsourcing.domain.task.dto.request;

import java.time.LocalDateTime;

import com.example.spartaoutsourcing.domain.task.enums.TaskPriority;
import com.example.spartaoutsourcing.domain.task.enums.TaskStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TaskUpdateRequest {

	@NotBlank(message = "제목은 필수입니다.")
	private String title;

	@NotBlank(message = "설명은 필수입니다.")
	private String description;

	@NotNull(message = "마감일은 필수입니다.")
	private LocalDateTime dueDate;

	@NotNull(message = "우선순위는 필수입니다.")
	private TaskPriority priority;

	@NotNull(message = "작업 상태는 필수입니다.")
	private TaskStatus taskStatus;

	@NotNull(message = "담당자ID는 필수입니다.")
	private Long assigneeId;
}
