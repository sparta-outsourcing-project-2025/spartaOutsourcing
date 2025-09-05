package com.example.spartaoutsourcing.domain.dashboard.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DashboardResponse {

	private int totalTasks;
	private int completedTasks;
	private int inProgressTasks;
	private int todoTasks;
	private int overdueTasks;
	private int teamProgress;
	private int myTasksToday;
	private int completionRate;

	public static DashboardResponse of(int totalTasks, int completedTasks, int inProgressTasks, int todoTasks,
		int overdueTasks, int teamProgress, int myTasksToday, int completionRate) {

		return new DashboardResponse(totalTasks, completedTasks, inProgressTasks, todoTasks,
			overdueTasks, teamProgress, myTasksToday, completionRate);
	}
}
