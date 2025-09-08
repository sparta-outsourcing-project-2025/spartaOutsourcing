package com.example.spartaoutsourcing.domain.task.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.dto.AuthUserRequest;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.task.dto.request.TaskRequest;
import com.example.spartaoutsourcing.domain.task.dto.request.TaskStatusUpdateRequest;
import com.example.spartaoutsourcing.domain.task.dto.request.TaskUpdateRequest;
import com.example.spartaoutsourcing.domain.task.dto.response.Assignee;
import com.example.spartaoutsourcing.common.dto.PageResponseDto;
import com.example.spartaoutsourcing.domain.task.dto.response.TaskProjection;
import com.example.spartaoutsourcing.domain.task.dto.response.TaskResponse;
import com.example.spartaoutsourcing.domain.task.dto.response.TaskUserInfoResponse;
import com.example.spartaoutsourcing.domain.task.entity.Task;
import com.example.spartaoutsourcing.domain.task.enums.TaskStatus;
import com.example.spartaoutsourcing.domain.task.repository.TaskRepository;
import com.example.spartaoutsourcing.domain.user.entity.User;
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
	public TaskResponse save(AuthUserRequest authUserRequest, TaskRequest taskRequest) {
		User user = userService.getUserById(authUserRequest.getId());

		User getAssignee = userService.getUserById(taskRequest.getAssigneeId());

		Task task = Task.of(taskRequest.getTitle(), taskRequest.getDescription(), TaskStatus.TODO,
			taskRequest.getDueDate(), taskRequest.getPriority(), user, getAssignee);

		taskRepository.save(task);

		Assignee assignee = Assignee.of(getAssignee.getId(), getAssignee.getUsername(), getAssignee.getName(),
			getAssignee.getEmail());

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

		Task task = getTaskById(taskId);

		User getAssignee = task.getAssignee();

		Assignee assignee = Assignee.of(getAssignee.getId(), getAssignee.getUsername(), getAssignee.getName(),
			getAssignee.getEmail());

		return TaskResponse.of(task.getId(), task.getTitle(), task.getDescription(), task.getDueDate(),
			task.getTaskPriority(), task.getTaskStatus(), user.getId(), assignee, task.getCreatedAt(), task.getModifiedAt());
	}

	public TaskResponse update(AuthUserRequest authUserRequest, Long taskId, TaskUpdateRequest taskUpdateRequest) {
		User user = userService.getUserById(authUserRequest.getId());

		Task task = getTaskById(taskId);

		User newAssignee = userService.getUserById(taskUpdateRequest.getAssigneeId());

		task.update(taskUpdateRequest.getTitle(), taskUpdateRequest.getDescription(), taskUpdateRequest.getTaskStatus(),
			taskUpdateRequest.getDueDate(), taskUpdateRequest.getPriority(), newAssignee);

		taskRepository.save(task);

		Assignee assignee = Assignee.of(newAssignee.getId(), newAssignee.getUsername(), newAssignee.getName(), newAssignee.getEmail());

		return TaskResponse.of(task.getId(), task.getTitle(), task.getDescription(), task.getDueDate(),
			task.getTaskPriority(), task.getTaskStatus(), user.getId(), assignee, task.getCreatedAt(), task.getModifiedAt());
	}

	/**
	 * Task 전체 목록 조회
	 * 페이지네이션과 검색 기능
	 * */
	@Transactional(readOnly = true)
	public PageResponseDto<TaskResponse> getTasks(Long page, Long size, String status, String search, Long assigneeId) {
		Long offset = page * size;
		Long limit = size;

		List<TaskProjection> taskAll = taskRepository.findAll(status, search, assigneeId, offset, limit);

		List<TaskResponse> taskResponses = taskAll.stream().map(TaskResponse::from).collect(Collectors.toList());

		Long totalElements = taskRepository.countTasksAll();
		int totalPage= (int)Math.ceil((double)totalElements / size);
		return PageResponseDto.of(taskResponses, totalElements, totalPage, limit, page);
	}

	/**
	 * Task 상태 업데이트
	 * status의 ENUM값 변경
	 * **/
	public TaskResponse updateStatus(AuthUserRequest authUserRequest, Long taskId, TaskStatusUpdateRequest taskStatusUpdateRequest) {
		User user = userService.getUserById(authUserRequest.getId());

		Task task = getTaskById(taskId);

		task.statusUpdate(taskStatusUpdateRequest.getStatus());

		User getAssignee = task.getAssignee();

		Assignee assignee = Assignee.of(getAssignee.getId(), getAssignee.getUsername(), getAssignee.getName(),
			getAssignee.getEmail());

		return TaskResponse.of(task.getId(), task.getTitle(), task.getDescription(), task.getDueDate(),
			task.getTaskPriority(), task.getTaskStatus(), user.getId(), assignee, task.getCreatedAt(), task.getModifiedAt());
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

	/**
	 * 추가적으로 필요한 API
	 * Task 모든 사용자의 정보를 반환
	 * **/
	@Transactional(readOnly = true)
	public List<TaskUserInfoResponse> getTaskUserInfo() {
		List<User> users = userService.getUsers();

		return users.stream().map(user ->
			TaskUserInfoResponse.of(user.getId(), user.getEmail(), user.getName(), user.getRole())).collect(
			Collectors.toList());
	}

	/**
	 * Task 삭제
	 * soft delete를 이용해 논리적으로 삭제한다.
	 * **/
	public void delete(Long taskId) {
		Task task = taskRepository.findById(taskId).orElseThrow(() ->
			new GlobalException(ErrorCode.TASK_NOT_FOUND));

		task.softDelete();
	}

	@Transactional(readOnly = true)
	public Task getTaskById(Long taskId)
	{
		return  taskRepository.findById(taskId).orElseThrow(() ->
			new GlobalException(ErrorCode.TASK_NOT_FOUND));
	}
}
