package com.example.spartaoutsourcing.domain.dashboard.dto.response;


public interface DashboardStatsProjection {

	int getTotalTasks();

	int getCompletedTasks();

	int getTodoTasks();

	int getInProgressTasks();

	int getOverDueTasks();

	int getMyTasksToday();
}
