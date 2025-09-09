package com.example.spartaoutsourcing.domain.activity.controller;

import com.example.spartaoutsourcing.common.consts.SuccessCode;
import com.example.spartaoutsourcing.common.dto.GlobalApiResponse;
import com.example.spartaoutsourcing.common.dto.PageResponseDto;
import com.example.spartaoutsourcing.domain.activity.dto.response.ActivityContentResponse;
import com.example.spartaoutsourcing.domain.activity.enums.ActivityType;
import com.example.spartaoutsourcing.domain.activity.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @GetMapping("/api/activities")
    public GlobalApiResponse<PageResponseDto<ActivityContentResponse>> getActivities(
            @RequestParam(required = false) ActivityType type,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String taskId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @PageableDefault Pageable pageable) {
        return GlobalApiResponse.of(SuccessCode.SUCCESS_FIND_ACTIVITY,
                activityService.getActivities(
                        type,
                        userId,
                        taskId,
                        startDate,
                        endDate,
                        pageable
                ));
    }

}
