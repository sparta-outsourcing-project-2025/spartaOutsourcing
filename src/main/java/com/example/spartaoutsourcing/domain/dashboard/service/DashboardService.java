package com.example.spartaoutsourcing.domain.dashboard.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.dto.PageResponseDto;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.activity.repository.ActivityRepository;
import com.example.spartaoutsourcing.domain.dashboard.dto.response.DashboardActivitiesProjection;
import com.example.spartaoutsourcing.domain.dashboard.dto.response.DashboardActivitiesResponse;
import com.example.spartaoutsourcing.domain.dashboard.dto.response.DashboardMyTaskProjection;
import com.example.spartaoutsourcing.domain.dashboard.dto.response.DashboardMyTaskResponse;
import com.example.spartaoutsourcing.domain.dashboard.dto.response.DashboardOverDueTaskResponse;
import com.example.spartaoutsourcing.domain.dashboard.dto.response.DashboardResponse;
import com.example.spartaoutsourcing.domain.dashboard.dto.response.DashboardStatsProjection;
import com.example.spartaoutsourcing.domain.dashboard.dto.response.DashboardTodayTaskResponse;
import com.example.spartaoutsourcing.domain.dashboard.dto.response.DashboardUpcomingTaskResponse;
import com.example.spartaoutsourcing.domain.dashboard.dto.response.DashboardUserInfoResponse;
import com.example.spartaoutsourcing.domain.member.service.MemberService;
import com.example.spartaoutsourcing.domain.task.entity.Task;
import com.example.spartaoutsourcing.domain.task.enums.TaskStatus;
import com.example.spartaoutsourcing.domain.task.repository.TaskRepository;
import com.example.spartaoutsourcing.domain.team.entity.Team;
import com.example.spartaoutsourcing.domain.team.repository.TeamRepository;
import com.example.spartaoutsourcing.domain.user.entity.User;
import com.example.spartaoutsourcing.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

	private final TaskRepository taskRepository;
	private final TeamRepository teamRepository;
	private final MemberService memberService;
	private final ActivityRepository activityRepository;

	/**
	 * Dashboard 통계 조회
	 * **/

	public DashboardResponse getDashboardStats() {
		DashboardStatsProjection dashboardStats = taskRepository.findDashboardStats();

		Team team = teamRepository.findFirstByOrderByCreatedAtAsc().orElseThrow(() ->
			new GlobalException(ErrorCode.DASHBOARD_TEAM_NOT_FOUND));

		int teamProgress = getTeamProgress(team);

		List<Task> taskAll = taskRepository.findAll();
		int totalTask = taskAll.size();
		long completed = taskAll.stream().filter(t -> t.getTaskStatus() == TaskStatus.COMPLETED).count();
		int completionRate = totalTask == 0 ? 0 : (int)(((double)completed / totalTask) * 100);

		return DashboardResponse.of(dashboardStats.getTotalTasks(), dashboardStats.getCompletedTasks(),
			dashboardStats.getInProgressTasks(), dashboardStats.getTodoTasks(), dashboardStats.getOverDueTasks(),
			teamProgress, dashboardStats.getMyTasksToday(), completionRate);
	}

	private int getTeamProgress(Team team) {
		List<User> members = memberService.getMembersByTeamId(team.getId());

		int teamProgress = (int)members.stream().mapToDouble(member -> {
			List<Task> tasks = taskRepository.findByUser(member);
			int total = tasks.size();
			long completed = tasks.stream().filter(t -> t.getTaskStatus() == TaskStatus.COMPLETED).count();
			return total == 0 ? 0 : (completed * 100.0 / total);
		}).average().orElse(0);
		return teamProgress;
	}

	/**
	 * dashboard 내 작업 요약 조회
	 * **/
	public DashboardMyTaskResponse getDashboardMyTasks() {
		List<DashboardMyTaskProjection> myTaskAll = taskRepository.findMyTaskAll();

		List<DashboardTodayTaskResponse> todayTasks = myTaskAll.stream()
			.filter(today -> "TODAY".equals(today.getTaskCategory()))
			.filter(today -> today.getTaskStatus() != TaskStatus.COMPLETED)
			.map(today -> DashboardTodayTaskResponse.of(
				today.getId(), today.getTitle(), today.getTaskStatus(), today.getDueDate()))
			.collect(Collectors.toList());


		List<DashboardUpcomingTaskResponse> upcomingTasks = myTaskAll.stream()
			.filter(upcoming -> "UPCOMING".equals(upcoming.getTaskCategory()))
			.filter(upcoming -> upcoming.getTaskStatus() != TaskStatus.COMPLETED)
			.map(upcoming -> DashboardUpcomingTaskResponse.of(
				upcoming.getId(), upcoming.getTitle(), upcoming.getTaskStatus(), upcoming.getDueDate()))
			.collect(Collectors.toList());

		List<DashboardOverDueTaskResponse> overDueTasks = myTaskAll.stream()
			.filter(overDue -> "OVERDUE".equals(overDue.getTaskCategory()))
			.filter(overDue -> overDue.getTaskStatus() != TaskStatus.COMPLETED)
			.map(overDue -> DashboardOverDueTaskResponse.of(
				overDue.getId(), overDue.getTitle(), overDue.getTaskStatus(), overDue.getDueDate()))
			.collect(Collectors.toList());

		return DashboardMyTaskResponse.of(todayTasks, upcomingTasks, overDueTasks);
	}

	/**
	 * dashboard 팀 진행률 조회
	 * **/
	public Map<String, Integer> getDashboardTeamProgress() {
		List<Team> teams = teamRepository.findAll();
		Map<String, Integer> result = new HashMap<>();

		for (Team team : teams) {
			int teamProgress = getTeamProgress(team);
			result.put(team.getName(), teamProgress);
		}

		return result;
	}

	/**
	 * dashboard 최근 활동 조회
	 * **/
	public PageResponseDto<DashboardActivitiesResponse> getDashboardActivities(long page, long size) {
		long offset = page * size;
		long limit = size;

		List<DashboardActivitiesProjection> activityAll = activityRepository.findActivityAll(offset, limit);

		List<DashboardActivitiesResponse> list =
			activityAll.stream()
			.map(DashboardActivitiesResponse::from).toList();

		Long totalElements = activityRepository.countActivityAll();

		int totalPage= (int)Math.ceil((double)totalElements / size);

		return PageResponseDto.of(list, totalElements, totalPage, page, size);
	}
}
