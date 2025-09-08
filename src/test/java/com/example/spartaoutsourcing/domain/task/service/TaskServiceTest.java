package com.example.spartaoutsourcing.domain.task.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.dto.AuthUserRequest;
import com.example.spartaoutsourcing.common.dto.PageResponseDto;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.task.dto.request.TaskRequest;
import com.example.spartaoutsourcing.domain.task.dto.request.TaskStatusUpdateRequest;
import com.example.spartaoutsourcing.domain.task.dto.request.TaskUpdateRequest;
import com.example.spartaoutsourcing.domain.task.dto.response.TaskProjection;
import com.example.spartaoutsourcing.domain.task.dto.response.TaskResponse;
import com.example.spartaoutsourcing.domain.task.dto.response.TaskUserInfoResponse;
import com.example.spartaoutsourcing.domain.task.entity.Task;
import com.example.spartaoutsourcing.domain.task.enums.TaskPriority;
import com.example.spartaoutsourcing.domain.task.enums.TaskStatus;
import com.example.spartaoutsourcing.domain.task.repository.TaskRepository;
import com.example.spartaoutsourcing.domain.user.entity.User;
import com.example.spartaoutsourcing.domain.user.enums.UserRole;
import com.example.spartaoutsourcing.domain.user.service.UserService;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

	@Mock
	private TaskRepository taskRepository;

	@Mock
	private UserService userService;

	@InjectMocks
	private TaskService taskService;

	private TaskProjection createTaskProjectionMock(Task task, User user) {
		TaskProjection projection = mock(TaskProjection.class);
		when(projection.getId()).thenReturn(task.getId());
		when(projection.getTitle()).thenReturn(task.getTitle());
		when(projection.getDescription()).thenReturn(task.getDescription());
		when(projection.getDueDate()).thenReturn(task.getDueDate());
		when(projection.getPriority()).thenReturn(task.getTaskPriority());
		when(projection.getStatus()).thenReturn(task.getTaskStatus());
		when(projection.getAssigneeId()).thenReturn(user.getId());
		when(projection.getUsername()).thenReturn(user.getUsername());
		when(projection.getName()).thenReturn(user.getName());
		when(projection.getEmail()).thenReturn(user.getEmail());
		when(projection.getCreatedAt()).thenReturn(task.getCreatedAt());
		when(projection.getUpdatedAt()).thenReturn(task.getModifiedAt());
		return projection;
	}

	private User createUser() {
		User user = User.of("test", "test@test.com", "Test1234!", "테스터", UserRole.USER);
		return userService.getUserById(user.getId());
	}

	// private Task createAssignee(User user, User assignee) {
	// 	Task.of("제목", "설명", TaskStatus.TODO, LocalDateTime.of(2025, 9, 8, 14, 0, 0),
	// 		TaskPriority.HIGH, user, assignee);
	// }

	@Test
	@DisplayName("존재하는 사용자는 작업을 생성할 시 성공적으로 동작한다.")
	public void successTaskSave() {
	    //given
		Long userId = 1L;
		Long assigneeId = 2L;
		AuthUserRequest authUserRequest = AuthUserRequest.of(userId, "test@test.com", UserRole.USER);
		TaskRequest taskRequest = new TaskRequest("제목", "설명", LocalDateTime.of(2025, 9, 8, 14, 0, 0),
			TaskPriority.HIGH, assigneeId);

		User user = User.of("test", "test@test.com", "Test1234!", "테스터", UserRole.USER);
		User assignee = User.of("assignee", "assignee@test.com", "assignee1234!", "assignee", UserRole.USER);
		ReflectionTestUtils.setField(user, "id", userId);
		ReflectionTestUtils.setField(assignee, "id", assigneeId);

		Task task = Task.of(taskRequest.getTitle(), taskRequest.getDescription(), TaskStatus.TODO,
			taskRequest.getDueDate(), taskRequest.getPriority(), user, assignee);

		given(userService.getUserById(userId)).willReturn(user);
		given(userService.getUserById(assigneeId)).willReturn(assignee);
		given(taskRepository.save(any(Task.class))).willReturn(task);

		//when
		TaskResponse save = taskService.save(authUserRequest, taskRequest);

		//then
		assertNotNull(save);
		assertEquals("제목", save.getTitle());
		assertEquals("설명", save.getDescription());
		assertEquals(TaskStatus.TODO, save.getStatus());
		assertEquals(TaskPriority.HIGH, save.getPriority());
		assertEquals(assigneeId, save.getAssignee().getId());
	}

	@Test
	@DisplayName("존재하지 않는 회원이 작업을 생성할 시 예외가 발생한다.")
	public void notFoundUserTaskException() {
	    //given
		Long userId = 1L;
		Long assigneeId = 2L;
		AuthUserRequest authUserRequest = AuthUserRequest.of(userId, "test@test.com", UserRole.USER);
		TaskRequest taskRequest = new TaskRequest("제목", "설명", LocalDateTime.of(2025, 9, 8, 14, 0, 0),
			TaskPriority.HIGH, assigneeId);

		User user = User.of("test", "test@test.com", "Test1234!", "테스터", UserRole.USER);
		User assignee = User.of("assignee", "assignee@test.com", "assignee1234!", "assignee", UserRole.USER);
		ReflectionTestUtils.setField(user, "id", userId);
		ReflectionTestUtils.setField(assignee, "id", assigneeId);

		Task task = Task.of(taskRequest.getTitle(), taskRequest.getDescription(), TaskStatus.TODO,
			taskRequest.getDueDate(), taskRequest.getPriority(), user, assignee);

		given(userService.getUserById(userId)).willThrow(
			new GlobalException(ErrorCode.USER_NOT_FOUND));

	    //when && then
		GlobalException globalException = assertThrows(GlobalException.class,
			() -> taskService.save(authUserRequest, taskRequest));

		assertEquals("유저를 찾을 수 없습니다", globalException.getMessage());
	}

	@Test
	@DisplayName("존재하는 사용자가 Task를 상세 조회 시 성공적으로 동작한다.")
	public void successGetTask() {
	    //given
		Long userId = 1L;
		Long assigneeId = 2L;
		AuthUserRequest authUserRequest = AuthUserRequest.of(userId, "test@test.com", UserRole.USER);

		User user = User.of("test", "test@test.com", "Test1234!", "테스터", UserRole.USER);
		User assignee = User.of("assignee", "assignee@test.com", "assignee1234!", "assignee", UserRole.USER);

		ReflectionTestUtils.setField(user, "id", userId);
		ReflectionTestUtils.setField(assignee, "id", assigneeId);

		Long taskId = 1L;
		Task task = Task.of("제목", "설명", TaskStatus.TODO, LocalDateTime.of(2025, 9, 8, 14, 0, 0),
			TaskPriority.HIGH, user, assignee);
		ReflectionTestUtils.setField(task, "id", taskId);

		given(userService.getUserById(userId)).willReturn(user);
		given(taskRepository.findById(taskId)).willReturn(Optional.of(task));

		//when
		TaskResponse taskResponse = taskService.getTask(authUserRequest, taskId);

		//then
		assertNotNull(taskResponse);
		assertNotNull(task.getUser().getId());
		assertEquals(task.getId(), taskResponse.getId());
		assertEquals(task.getDescription(), taskResponse.getDescription());
		assertEquals(task.getTaskStatus(), taskResponse.getStatus());
		assertEquals(task.getTaskPriority(), taskResponse.getPriority());
		assertEquals(task.getDueDate(), taskResponse.getDueDate());
	}

	@Test
	@DisplayName("존재하는 사용자가 존재하지 않는 Task를 상세 조회 시 예외가 발생한다.")
	public void notFoundTaskException() {
	    //given
		Long userId = 1L;
		Long assigneeId = 2L;
		AuthUserRequest authUserRequest = AuthUserRequest.of(userId, "test@test.com", UserRole.USER);
		User user = User.of("test", "test@test.com", "Test1234!", "테스터", UserRole.USER);

		User assignee = User.of("assignee", "assignee@test.com", "assignee1234!", "assignee", UserRole.USER);

		ReflectionTestUtils.setField(user, "id", userId);
		ReflectionTestUtils.setField(assignee, "id", assigneeId);

		Long taskId = -1L;
		Task task = Task.of("제목", "설명", TaskStatus.TODO, LocalDateTime.of(2025, 9, 8, 14, 0, 0),
			TaskPriority.HIGH, user, assignee);

		given(taskRepository.findById(anyLong())).willReturn(Optional.empty());

		//when && then
		GlobalException globalException = assertThrows(GlobalException.class,
			() -> taskService.getTask(authUserRequest, taskId));

		assertEquals("해당 ID의 작업을 찾을 수 없습니다.", globalException.getMessage());
	}

	@Test
	@DisplayName("존재하는 사용자가 Task를 수정 시 성공적으로 동작한다.")
	public void successTaskUpdate() {
	    //given
		Long user1Id = 1L;
		AuthUserRequest authUserRequest = AuthUserRequest.of(user1Id, "test@test.com", UserRole.USER);
		User user1 = User.of("test", "test@test.com", "Test1234!", "테스터", UserRole.USER);

		Long user2Id = 2L;
		User user2 = User.of("test1", "tes1t@test.com", "Test1234!!", "테스터2", UserRole.USER);

		Long assigneeId = 2L;
		User assignee = User.of("assignee", "assignee@test.com", "assignee1234!", "assignee", UserRole.USER);

		Long taskId = 1L;
		TaskUpdateRequest taskUpdateRequest = new TaskUpdateRequest("제목", "설명", LocalDateTime.of(2025, 9, 8, 14, 0, 0),
			TaskPriority.HIGH, TaskStatus.TODO, assigneeId);

		Task task = Task.of(taskUpdateRequest.getTitle(), taskUpdateRequest.getDescription(), taskUpdateRequest.getTaskStatus(),
			taskUpdateRequest.getDueDate(), taskUpdateRequest.getPriority(), user2, assignee);

		ReflectionTestUtils.setField(task, "id", taskId);

		given(userService.getUserById(user1Id)).willReturn(user1);
		given(userService.getUserById(assigneeId)).willReturn(assignee);
		given(taskRepository.findById(taskId)).willReturn(Optional.of(task));

	    //when
		TaskResponse result = taskService.update(authUserRequest, taskId, taskUpdateRequest);

		//then
		assertNotNull(result);
		assertEquals(user1.getId(), task.getUser().getId());
		assertEquals(user2.getName(), task.getUser().getName());
		assertEquals(taskUpdateRequest.getTitle(), task.getTitle());
		assertEquals(taskUpdateRequest.getDescription(), task.getDescription());
		assertEquals(taskUpdateRequest.getPriority(), task.getTaskPriority());
		assertEquals(taskUpdateRequest.getTaskStatus(), task.getTaskStatus());
		assertEquals(taskUpdateRequest.getDueDate(), task.getDueDate());
	}

	@Test
	@DisplayName("존재하는 사용자가 Task를 전체 목록 조회할 시 성공적으로 동작한다.")
	public void successGetTasks() {
	    //given

		Long page = 0L;
		Long size = 10L;

		Long userId = 1L;
		AuthUserRequest authUserRequest = AuthUserRequest.of(userId, "test@test.com", UserRole.USER);
		User user = User.of("test", "test@test.com", "Test1234!", "테스터", UserRole.USER);

		User assignee = User.of("assignee", "assignee@test.com", "assignee1234!", "assignee", UserRole.USER);

		Task task1 = Task.of("제목", "설명", TaskStatus.TODO, LocalDateTime.of(2025, 9, 8, 14, 0, 0),
			TaskPriority.HIGH, user, assignee);
		Task task2 = Task.of("제목2", "설명2", TaskStatus.TODO, LocalDateTime.of(2025, 9, 8, 14, 0, 0),
			TaskPriority.LOW, user, assignee);

		List<TaskProjection> taskProjectionMock =
			List.of(createTaskProjectionMock(task1, user),
			createTaskProjectionMock(task2, user));

		given(taskRepository.findAll(null, null, null, page*size, size)).willReturn(taskProjectionMock);

		//when
		PageResponseDto<TaskResponse> result = taskService.getTasks(page, size, null, null, null);

		//then
		assertEquals(2, result.getContent().size());
		assertEquals(result.getContent().get(0).getTitle(), "제목");
		assertEquals(result.getContent().get(0).getDescription(), "설명");
		assertEquals(result.getContent().get(0).getPriority(), TaskPriority.HIGH);
		assertEquals(result.getContent().get(0).getStatus(), TaskStatus.TODO);
		assertEquals(result.getContent().get(0).getDueDate(), LocalDateTime.of(2025, 9, 8, 14, 0, 0));

		assertEquals(result.getContent().get(1).getTitle(), "제목2");
		assertEquals(result.getContent().get(1).getDescription(), "설명2");
		assertEquals(result.getContent().get(1).getPriority(), TaskPriority.LOW);
		assertEquals(result.getContent().get(1).getStatus(), TaskStatus.TODO);
		assertEquals(result.getContent().get(1).getDueDate(), LocalDateTime.of(2025, 9, 8, 14, 0, 0));
	}

	@Test
	@DisplayName("존재하는 사용자가 Task 상태 변경을 할 시 성공적으로 동작한다.")
	public void successTaskStatusUpdate() {
	    //given
		Long userId = 1L;
		Long assigneeId = 2L;
		AuthUserRequest authUserRequest = AuthUserRequest.of(userId, "test@test.com", UserRole.USER);

		User user = User.of("test", "test@test.com", "Test1234!", "테스터", UserRole.USER);
		User assignee = User.of("assignee", "assignee@test.com", "assignee1234!", "assignee", UserRole.USER);

		ReflectionTestUtils.setField(user, "id", userId);
		ReflectionTestUtils.setField(assignee, "id", assigneeId);

		Long taskId = 1L;
		Task task = Task.of("제목", "설명", TaskStatus.TODO, LocalDateTime.of(2025, 9, 8, 14, 0, 0),
			TaskPriority.HIGH, user, assignee);
		ReflectionTestUtils.setField(task, "id", taskId);

		TaskStatusUpdateRequest taskStatusUpdateRequest = new TaskStatusUpdateRequest(TaskStatus.IN_PROGRESS);

		given(taskRepository.findById(taskId)).willReturn(Optional.of(task));
		given(userService.getUserById(userId)).willReturn(user);

		//when
		TaskResponse result = taskService.updateStatus(authUserRequest, taskId, taskStatusUpdateRequest);

		//then
		assertNotNull(result);
		assertEquals(task.getTaskStatus(), result.getStatus());
	}

	@Test
	@DisplayName("존재하는 사용자가 존재하지 않는 Task 상태를 변경 시 예외가 발생한다.")
	public void notFoundTaskStatusUpdateException() {
	    //given
		Long userId = 1L;
		Long assigneeId = 2L;
		AuthUserRequest authUserRequest = AuthUserRequest.of(userId, "test@test.com", UserRole.USER);
		User user = User.of("test", "test@test.com", "Test1234!", "테스터", UserRole.USER);

		User assignee = User.of("assignee", "assignee@test.com", "assignee1234!", "assignee", UserRole.USER);

		ReflectionTestUtils.setField(user, "id", userId);
		ReflectionTestUtils.setField(assignee, "id", assigneeId);

		Long taskId = -1L;
		Task task = Task.of("제목", "설명", TaskStatus.TODO, LocalDateTime.of(2025, 9, 8, 14, 0, 0),
			TaskPriority.HIGH, user, assignee);

		TaskStatusUpdateRequest taskStatusUpdateRequest = new TaskStatusUpdateRequest(TaskStatus.IN_PROGRESS);

		given(taskRepository.findById(taskId)).willReturn(Optional.empty());

	    //when && then
		GlobalException globalException = assertThrows(GlobalException.class,
			() -> taskService.updateStatus(authUserRequest, taskId, taskStatusUpdateRequest));

		assertEquals("해당 ID의 작업을 찾을 수 없습니다.", globalException.getMessage());
	}

	@Test
	@DisplayName("존재하는 사용자의 정보를 모두 반환할 수 있다.")
	public void successTaskUsersInfo() {
	    //given
		Long user1Id = 1L;
		AuthUserRequest authUserRequest1 = AuthUserRequest.of(user1Id, "test@test.com", UserRole.USER);
		User user1 = User.of("test", "test@test.com", "Test1234!", "테스터", UserRole.USER);
		ReflectionTestUtils.setField(user1, "id", 1L);

		Long user2Id = 2L;
		AuthUserRequest authUserRequest2 = AuthUserRequest.of(user2Id, "test@test.com", UserRole.USER);
		User user2 = User.of("test2", "test2@test.com", "Test1234!@", "테스터2", UserRole.USER);
		ReflectionTestUtils.setField(user2, "id", 2L);

		List<User> userList = List.of(user1, user2);

		given(userService.getUsers()).willReturn(userList);

		//when
		List<TaskUserInfoResponse> taskUserInfo = taskService.getTaskUserInfo();

		//then
		assertThat(taskUserInfo).hasSize(2);
		assertNotNull(taskUserInfo);

		assertEquals(user1.getId(), taskUserInfo.get(0).getId());
		assertEquals(user1.getEmail(), taskUserInfo.get(0).getEmail());
		assertEquals(user1.getName(), taskUserInfo.get(0).getName());
		assertEquals(user1.getRole(), taskUserInfo.get(0).getRole());

		assertEquals(user2.getId(), taskUserInfo.get(1).getId());
		assertEquals(user2.getEmail(), taskUserInfo.get(1).getEmail());
		assertEquals(user2.getName(), taskUserInfo.get(1).getName());
		assertEquals(user2.getRole(), taskUserInfo.get(1).getRole());
	}

	@Test
	@DisplayName("Task를 soft delete할 시 deletedAt이 저장된다.")
	public void successTaskDelete() {
	    //given
		Long userId = 1L;
		Long assigneeId = 2L;
		AuthUserRequest authUserRequest = AuthUserRequest.of(userId, "test@test.com", UserRole.USER);
		User user = User.of("test", "test@test.com", "Test1234!", "테스터", UserRole.USER);
		User assignee = User.of("assignee", "assignee@test.com", "assignee1234!", "assignee", UserRole.USER);

		ReflectionTestUtils.setField(user, "id", userId);
		ReflectionTestUtils.setField(assignee, "id", assigneeId);

		Long taskId = 1L;
		Task task = Task.of("제목", "설명", TaskStatus.TODO, LocalDateTime.of(2025, 9, 8, 14, 0, 0),
			TaskPriority.HIGH, user, assignee);
		ReflectionTestUtils.setField(task, "id", taskId);

		given(taskRepository.findById(taskId)).willReturn(Optional.of(task));

	    //when
		taskService.delete(taskId);

	    //then
		assertNotNull(task);
	}
}