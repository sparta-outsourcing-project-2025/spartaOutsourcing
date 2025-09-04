package com.example.spartaoutsourcing.domain.task.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.spartaoutsourcing.common.annotation.Auth;
import com.example.spartaoutsourcing.common.consts.SuccessCode;
import com.example.spartaoutsourcing.common.dto.AuthUserRequest;
import com.example.spartaoutsourcing.common.dto.GlobalApiResponse;
import com.example.spartaoutsourcing.domain.task.dto.request.TaskRequest;
import com.example.spartaoutsourcing.domain.task.dto.request.TaskStatusUpdateRequest;
import com.example.spartaoutsourcing.domain.task.dto.request.TaskUpdateRequest;
import com.example.spartaoutsourcing.domain.comment.dto.response.PageResponseDto;
import com.example.spartaoutsourcing.domain.task.dto.response.TaskResponse;
import com.example.spartaoutsourcing.domain.task.dto.response.TaskUserInfoResponse;
import com.example.spartaoutsourcing.domain.task.service.TaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TaskController {

	private final TaskService taskService;

	@PostMapping("/tasks")
	public GlobalApiResponse<TaskResponse> save(@Auth AuthUserRequest authUserRequest, @Valid @RequestBody TaskRequest taskRequest) {
		TaskResponse save = taskService.save(authUserRequest,taskRequest);
		return GlobalApiResponse.of(SuccessCode.TASK_CREATED, save);
	}

	@GetMapping("/tasks/{taskId}")
	public GlobalApiResponse<TaskResponse> getTask(@Auth AuthUserRequest authUserRequest, @PathVariable Long taskId) {
		TaskResponse task = taskService.getTask(authUserRequest, taskId);
		return GlobalApiResponse.of(SuccessCode.TASK_FIND, task);
	}

	@PutMapping("/tasks/{taskId}")
	public GlobalApiResponse<TaskResponse> update(@Auth AuthUserRequest authUserRequest, @PathVariable Long taskId,
		@Valid @RequestBody TaskUpdateRequest taskUpdateRequest) {
		TaskResponse update = taskService.update(authUserRequest, taskId, taskUpdateRequest);
		return GlobalApiResponse.of(SuccessCode.TASK_UPDATED, update);
	}

	@GetMapping("/tasks")
	public GlobalApiResponse<PageResponseDto<TaskResponse>> getTasks(@Auth AuthUserRequest authUserRequest,
		@RequestParam(defaultValue = "1") Long page, @RequestParam(defaultValue = "10") Long size, String status, String search, Long assigneeId) {
		PageResponseDto<TaskResponse> tasks = taskService.getTasks(page, size, status, search, assigneeId);

		return GlobalApiResponse.of(SuccessCode.TASK_FIND_ALL, tasks);
	}

	@PatchMapping("/tasks/{taskId}/status")
	public GlobalApiResponse<TaskResponse> statusUpdate(@Auth AuthUserRequest authUserRequest, @PathVariable Long taskId,
		@Valid @RequestBody TaskStatusUpdateRequest taskStatusUpdateRequest) {
		TaskResponse statusUpdate = taskService.statusUpdate(authUserRequest, taskId, taskStatusUpdateRequest);

		return GlobalApiResponse.of(SuccessCode.TASK_STATUS_UPDATED, statusUpdate);
	}

	@GetMapping("/users")
	public GlobalApiResponse<TaskUserInfoResponse> getTaskUserInfo(@Auth AuthUserRequest authUserRequest) {
		TaskUserInfoResponse taskUserInfo = taskService.getTaskUserInfo(authUserRequest);

		return GlobalApiResponse.of(SuccessCode.OK, taskUserInfo);
	}
}
