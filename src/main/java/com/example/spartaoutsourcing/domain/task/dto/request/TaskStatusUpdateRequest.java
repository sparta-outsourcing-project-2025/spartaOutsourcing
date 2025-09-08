package com.example.spartaoutsourcing.domain.task.dto.request;

import com.example.spartaoutsourcing.domain.task.enums.TaskStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskStatusUpdateRequest {

	@NotNull(message = "상태값은 필수입니다.")
	private TaskStatus status;
}
