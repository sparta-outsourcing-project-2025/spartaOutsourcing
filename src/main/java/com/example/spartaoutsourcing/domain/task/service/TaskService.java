package com.example.spartaoutsourcing.domain.task.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.task.dto.request.TaskRequest;
import com.example.spartaoutsourcing.domain.task.dto.response.Assignee;
import com.example.spartaoutsourcing.domain.task.dto.response.TaskResponse;
import com.example.spartaoutsourcing.domain.task.entity.Task;
import com.example.spartaoutsourcing.domain.task.enums.TaskStatus;
import com.example.spartaoutsourcing.domain.task.repository.TaskRepository;
import com.example.spartaoutsourcing.domain.user.entity.User;
import com.example.spartaoutsourcing.domain.user.repository.UserRepository;
import com.example.spartaoutsourcing.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

	private final TaskRepository taskRepository;
	private final UserService userService;

	/**
	* 사용자 Task 생성
	 * @RequestBody Task 요청 정보
	 * @return 공통 응답과 Task 생성 응답 반환
	*/
	public TaskResponse save(TaskRequest taskRequest) {
		User user = userService.getUserById(taskRequest.getAssigneeId());

		Task task = Task.of(taskRequest.getTitle(), taskRequest.getDescription(), TaskStatus.TODO,
			taskRequest.getDueDate(), taskRequest.getPriority(), user);

		taskRepository.save(task);

		Assignee assignee = Assignee.of(user.getId(), user.getUsername(), user.getName(), user.getEmail());

		return TaskResponse.of(task.getId(), task.getTitle(), task.getDescription(), task.getDueDate(),
			task.getTaskPriority(), task.getTaskStatus(), user.getId(), assignee, task.getCreatedAt(), task.getModifiedAt());
	}
}
