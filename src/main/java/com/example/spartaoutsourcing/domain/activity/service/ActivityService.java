package com.example.spartaoutsourcing.domain.activity.service;

import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.dto.PageResponseDto;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.activity.dto.response.ActivityContentResponse;
import com.example.spartaoutsourcing.domain.activity.entity.Activity;
import com.example.spartaoutsourcing.domain.activity.enums.ActivityType;
import com.example.spartaoutsourcing.domain.activity.repository.ActivityRepository;
import com.example.spartaoutsourcing.domain.user.dto.response.UserResponse;
import com.example.spartaoutsourcing.domain.user.entity.User;
import com.example.spartaoutsourcing.domain.user.service.UserService;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activitiesRepository;
    private final UserService userService;
    private final EntityManager em;

    @Transactional
    public Activity save(ActivityType type, User user, Long taskId, String description){
        Activity activity = Activity.of(type, user, taskId, description);
        return activitiesRepository.save(activity);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<ActivityContentResponse> getActivities(
            ActivityType type,
            String userId,
            String taskId,
            String startDate,
            String endDate,
            Pageable pageable){

        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        User user = null;
        Long task_id = null;

        try{
        if(startDate != null)
            startDateTime = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();

        if(endDate != null)
            endDateTime = LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE).atTime(LocalTime.MAX);

        if(userId != null)
            user = userService.getUserById(Long.parseLong(userId));

        if(taskId != null)
            task_id = Long.parseLong(taskId);
        }catch(Exception e){
            throw new GlobalException(ErrorCode.INVALID_REQUEST_PARAMETER);
        }
        Page<Activity> activityPage = activitiesRepository.findByTypeAndUserIdAndTaskIdAndCreatedAtBetween(
                type,
                user,
                task_id,
                startDateTime,
                endDateTime,
                pageable
        );

        List<ActivityContentResponse> activityContentResponse = activityPage.map(
                activity ->  ActivityContentResponse.of(
                            activity.getId(),
                            activity.getType(),
                            activity.getUser().getId(),
                            UserResponse.from(activity.getUser()),
                            activity.getTaskId(),
                            LocalDateTime.now(),
                            activity.getDescription())
                ).toList();

        return PageResponseDto.of(
                activityContentResponse,
                activityPage.getTotalElements(),
                activityPage.getTotalPages(),
                pageable.getPageSize(),
                pageable.getPageNumber()
        );
    }
}
