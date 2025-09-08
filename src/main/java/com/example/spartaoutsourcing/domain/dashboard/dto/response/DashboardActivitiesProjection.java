package com.example.spartaoutsourcing.domain.dashboard.dto.response;

import java.time.LocalDateTime;

import com.example.spartaoutsourcing.domain.activity.enums.ActivityType;

public interface DashboardActivitiesProjection {
	Long getId();
	Long getUserId();
	String getName();
	ActivityType getAction();
	String getTargetType();
	Long getTargetId();
	String getDescription();
	LocalDateTime getCreatedAt();
}
