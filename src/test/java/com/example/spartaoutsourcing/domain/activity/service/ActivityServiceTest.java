package com.example.spartaoutsourcing.domain.activity.service;

import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.dto.PageResponseDto;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.activity.dto.response.ActivityContentResponse;
import com.example.spartaoutsourcing.domain.activity.entity.Activity;
import com.example.spartaoutsourcing.domain.activity.enums.ActivityType;
import com.example.spartaoutsourcing.domain.activity.repository.ActivityRepository;
import com.example.spartaoutsourcing.domain.user.entity.User;
import com.example.spartaoutsourcing.domain.user.enums.UserRole;
import com.example.spartaoutsourcing.domain.user.repository.UserRepository;
import com.example.spartaoutsourcing.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import static org.mockito.Mockito.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityRepository activitiesRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ActivityService activityService;

    @Test
    @DisplayName("활동 로그 저장 후 엔티티를 반환")
    void testSave_Success() {
        // given
        Long id = 1L;
        ActivityType type = ActivityType.TASK_CREATED;
        Long taskId = 1L;
        String description = "description";
        User user = User.of("username","email@email.com","password","name",UserRole.USER);
        Activity activityToSave = Activity.of(type, user, taskId,description);

        Activity savedActivityInDb = Activity.of(type, user, taskId,description);
        ReflectionTestUtils.setField(savedActivityInDb, "id", id);

        when(activitiesRepository.save(activityToSave)).thenReturn(savedActivityInDb);

        // when
        Activity savedActivity = activityService.save(activityToSave);

        // then
        assertThat(savedActivity.getId()).isNotNull();
        assertThat(savedActivity.getType()).isEqualTo(type);
        assertThat(savedActivity.getUser()).isEqualTo(user);
        assertThat(savedActivity.getTaskId()).isEqualTo(taskId);
        assertThat(savedActivity.getDescription()).isEqualTo(description);
    }

    @Test
    @DisplayName("활동 로그를 조건부 조회해 페이지로 반환")
    void testGetActivities_Success() {
        // given
        String userId = "1";
        String taskId = "1";
        String startDate = "2025-01-01";
        String endDate = "2025-01-02";
        Pageable pageable = PageRequest.of(0, 10);

        User mockUser = User.of("username", "email@email.com", "pw", "name", UserRole.USER);
        ReflectionTestUtils.setField(mockUser, "id", 1L);

        Activity mockActivity = Activity.of(
                ActivityType.TASK_CREATED,
                mockUser,
                1L,
                "description"
        );
        ReflectionTestUtils.setField(mockActivity, "id", 1L);

        Page<Activity> mockPage = new PageImpl<>(List.of(mockActivity), pageable, 1);

        given(userService.getUserById(1L)).willReturn(mockUser);
        given(activitiesRepository.findByTypeAndUserIdAndTaskIdAndCreatedAtBetween(
                any(), any(), any(), any(), any(), any()
        )).willReturn(mockPage);

        // when
        PageResponseDto<ActivityContentResponse> result = activityService.getActivities(
                ActivityType.TASK_CREATED,
                userId,
                taskId,
                startDate,
                endDate,
                pageable
        );

        // then
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals("description", result.getContent().get(0).getDescription());

        verify(userService, times(1)).getUserById(1L);
        verify(activitiesRepository, times(1))
                .findByTypeAndUserIdAndTaskIdAndCreatedAtBetween(any(), any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("활동 로그를 조건부 조회 실패 시 잘못된 요청 파라미터 예외 반환")
    void testGetActivities_ThrowExceptionWhenInvalidRequestParameter() {
        // given
        String userId = "error";
        String taskId = "1";
        String startDate = "2025-01-01";
        String endDate = "2025-01-02";
        Pageable pageable = PageRequest.of(0, 10);

        // when, then
        GlobalException exception = assertThrows(GlobalException.class, () -> activityService.getActivities(
                ActivityType.TASK_CREATED,
                userId,
                taskId,
                startDate,
                endDate,
                pageable
                ));
        assertEquals(ErrorCode.INVALID_REQUEST_PARAMETER, exception.getErrorCode());
    }
}