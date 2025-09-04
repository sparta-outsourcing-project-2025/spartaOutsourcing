package com.example.spartaoutsourcing.domain.task.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.dto.AuthUserRequest;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.task.dto.request.TaskRequest;
import com.example.spartaoutsourcing.domain.task.dto.request.TaskUpdateRequest;
import com.example.spartaoutsourcing.domain.task.dto.response.Assignee;
import com.example.spartaoutsourcing.domain.task.dto.response.PageResponseDto;
import com.example.spartaoutsourcing.domain.task.dto.response.TaskProjection;
import com.example.spartaoutsourcing.domain.task.dto.response.TaskResponse;
import com.example.spartaoutsourcing.domain.task.entity.Task;
import com.example.spartaoutsourcing.domain.task.enums.TaskStatus;
import com.example.spartaoutsourcing.domain.task.repository.TaskRepository;
import com.example.spartaoutsourcing.domain.user.entity.User;
import com.example.spartaoutsourcing.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

import java.util.List;

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
	public TaskResponse save(AuthUserRequest authUserRequest, TaskRequest taskRequest) {
		User user = userService.getUserById(authUserRequest.getId());

		Task task = Task.of(taskRequest.getTitle(), taskRequest.getDescription(), TaskStatus.TODO,
			taskRequest.getDueDate(), taskRequest.getPriority(), user);

		taskRepository.save(task);

		Assignee assignee = Assignee.of(user.getId(), user.getUsername(), user.getName(), user.getEmail());

		return TaskResponse.of(task.getId(), task.getTitle(), task.getDescription(), task.getDueDate(),
			task.getTaskPriority(), task.getTaskStatus(), user.getId(), assignee, task.getCreatedAt(), task.getModifiedAt());
	}

	/**
	 * 사용자 Task 상세 조회
	 * @PathVariable Task Id 요청
	 * @return 공통 응답과 Task 상세 조회 응답 반환
	 * */

	@Transactional(readOnly = true)
	public TaskResponse getTask(AuthUserRequest authUserRequest, Long taskId) {
		User user = userService.getUserById(authUserRequest.getId());

		Task task = taskRepository.findById(taskId).orElseThrow(() ->
			new GlobalException(ErrorCode.TASK_NOT_FOUND));

		Assignee assignee = Assignee.of(user.getId(), user.getUsername(), user.getName(), user.getEmail());

		return TaskResponse.of(task.getId(), task.getTitle(), task.getDescription(), task.getDueDate(),
			task.getTaskPriority(), task.getTaskStatus(), user.getId(), assignee, task.getCreatedAt(), task.getModifiedAt());
	}

	public TaskResponse update(AuthUserRequest authUserRequest, Long taskId, TaskUpdateRequest taskUpdateRequest) {
		User user = userService.getUserById(authUserRequest.getId());

		Task task = taskRepository.findById(taskId).orElseThrow(() ->
			new GlobalException(ErrorCode.TASK_NOT_FOUND));

		task.update(taskUpdateRequest.getTitle(), taskUpdateRequest.getDescription(), taskUpdateRequest.getTaskStatus(),
			taskUpdateRequest.getDueDate(), taskUpdateRequest.getPriority());

		taskRepository.save(task);

		Assignee assignee = Assignee.of(user.getId(), user.getUsername(), user.getName(), user.getEmail());

		return TaskResponse.of(task.getId(), task.getTitle(), task.getDescription(), task.getDueDate(),
			task.getTaskPriority(), task.getTaskStatus(), user.getId(), assignee, task.getCreatedAt(), task.getModifiedAt());
	}

	/**
	 * Task 전체 목록 조회
	 * 페이지네이션과 검색 기능
	 * */
	public PageResponseDto<TaskResponse> getTasks(long page, long size, String status, String search, Long assigneeId) {

		long offset = (page - 1) * size;
		long limit = size;

		List<TaskProjection> taskAll = taskRepository.findAll(status, search, assigneeId, offset, limit);

		List<TaskResponse> taskResponses = taskAll.stream().map(TaskResponse::from).collect(Collectors.toList());

		Long totalElements = taskRepository.countTasksAll();

		return PageResponseDto.of(taskResponses, totalElements, limit, page);
	}

	/**
	 * {@link Task}들을 제목, 내용으로 조회한다
	 *
	 * @param keyword 조회할 키워드
	 * @return 조회된 {@link Task} 리스트
	 */
	@Transactional(readOnly = true)
	public List<Task> getTasksByKeyword(String keyword)
	{
		return taskRepository.findTasksByKeyword(keyword);
	}

	/**
	 * {@link Task}들을 제목, 내용으로 조회해 페이지로 반환한다
	 *
	 * @param keyword 조회할 키워드
	 * @param pageable 페이지 옵션
	 * @return 조회된 {@link Task} 페이지
	 */
	@Transactional(readOnly = true)
	public Page<Task> getTaskPageByKeyword(String keyword, Pageable pageable)
	{
		return taskRepository.findTaskPageByKeyword(keyword, pageable);
	}
}
