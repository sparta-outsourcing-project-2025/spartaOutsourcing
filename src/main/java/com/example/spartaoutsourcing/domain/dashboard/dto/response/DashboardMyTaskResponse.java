package com.example.spartaoutsourcing.domain.dashboard.dto.response;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DashboardMyTaskResponse {

	private List<DashboardTodayTaskResponse> todayTaskResponses;
	private List<DashboardUpcomingTaskResponse> upcomingTaskResponses;
	private List<DashboardOverDueTaskResponse> overDueTaskResponses;

	public static DashboardMyTaskResponse of(
		List<DashboardTodayTaskResponse> todayTaskResponses,
		List<DashboardUpcomingTaskResponse> upcomingTaskResponses,
		List<DashboardOverDueTaskResponse> overDueTaskResponses) {

		return new DashboardMyTaskResponse(todayTaskResponses, upcomingTaskResponses, overDueTaskResponses);
	}
}
