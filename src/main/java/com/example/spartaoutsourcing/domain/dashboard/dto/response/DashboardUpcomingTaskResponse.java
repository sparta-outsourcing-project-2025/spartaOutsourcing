package com.example.spartaoutsourcing.domain.dashboard.dto.response;

import java.time.LocalDateTime;

import com.example.spartaoutsourcing.domain.task.enums.TaskStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DashboardUpcomingTaskResponse {

	private Long id;
	private String title;
	private TaskStatus taskStatus;
	private LocalDateTime dueDate;

	public static DashboardUpcomingTaskResponse of(Long id, String title, TaskStatus taskStatus, LocalDateTime dueDate) {
		return new DashboardUpcomingTaskResponse(id, title, taskStatus, dueDate);
	}
}
