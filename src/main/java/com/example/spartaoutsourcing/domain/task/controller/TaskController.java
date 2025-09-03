package com.example.spartaoutsourcing.domain.task.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spartaoutsourcing.common.annotation.Auth;
import com.example.spartaoutsourcing.common.consts.SuccessCode;
import com.example.spartaoutsourcing.common.dto.AuthUserRequest;
import com.example.spartaoutsourcing.common.dto.GlobalApiResponse;
import com.example.spartaoutsourcing.domain.task.dto.request.TaskRequest;
import com.example.spartaoutsourcing.domain.task.dto.response.TaskResponse;
import com.example.spartaoutsourcing.domain.task.service.TaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

	private final TaskService taskService;

	@PostMapping
	public GlobalApiResponse<TaskResponse> save(@Auth AuthUserRequest authUserRequest, @Valid @RequestBody TaskRequest taskRequest) {
		TaskResponse save = taskService.save(taskRequest);
		return GlobalApiResponse.of(SuccessCode.TASK_CREATED, save);
	}
}
