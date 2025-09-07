package com.example.spartaoutsourcing.domain.dashboard.dto.response;

import java.time.LocalDateTime;

import com.example.spartaoutsourcing.domain.activity.enums.ActivityType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DashboardActivitiesResponse {

	private Long id;
	private Long userId;
	private DashboardUserInfoResponse user;
	private ActivityType action;
	private String targetType;
	private Long targetId;
	private String description;
	private LocalDateTime createdAt;

	public static DashboardActivitiesResponse from(DashboardActivitiesProjection dashboardActivitiesProjection) {
		DashboardUserInfoResponse userInfo = DashboardUserInfoResponse.of(
			dashboardActivitiesProjection.getUserId(), dashboardActivitiesProjection.getName());

		return DashboardActivitiesResponse.of(
			dashboardActivitiesProjection.getId(), dashboardActivitiesProjection.getUserId(),
			userInfo, dashboardActivitiesProjection.getAction(), dashboardActivitiesProjection.getTargetType(),
			dashboardActivitiesProjection.getTargetId(), dashboardActivitiesProjection.getDescription(),
			dashboardActivitiesProjection.getCreatedAt()

		);
	}

	public static DashboardActivitiesResponse of(Long id, Long userId, DashboardUserInfoResponse user,
		ActivityType activityType, String targetType, Long targetId, String description, LocalDateTime createdAt) {

		return new DashboardActivitiesResponse(id, userId, user, activityType, targetType, targetId, description, createdAt);
	}
}
