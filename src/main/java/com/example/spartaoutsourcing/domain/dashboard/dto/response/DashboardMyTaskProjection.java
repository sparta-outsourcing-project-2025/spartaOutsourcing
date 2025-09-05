package com.example.spartaoutsourcing.domain.dashboard.dto.response;

import java.time.LocalDateTime;

import com.example.spartaoutsourcing.domain.task.enums.TaskStatus;

public interface DashboardMyTaskProjection {
	Long getId();
	String getTitle();
	TaskStatus getTaskStatus();
	LocalDateTime getDueDate();
	String getTaskCategory();
}
