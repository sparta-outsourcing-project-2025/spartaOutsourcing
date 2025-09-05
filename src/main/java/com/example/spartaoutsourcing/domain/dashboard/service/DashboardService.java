package com.example.spartaoutsourcing.domain.dashboard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.dashboard.dto.response.DashboardResponse;
import com.example.spartaoutsourcing.domain.dashboard.dto.response.DashboardStatsProjection;
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
	private final UserService userService;
	private final MemberService memberService;

	/**
	 * Dashboard 통계 조회
	 * **/

	public DashboardResponse getDashboardStats() {
		DashboardStatsProjection dashboardStats = taskRepository.findDashboardStats();

		Team team = teamRepository.findFirstByOrderByCreatedAtAsc().orElseThrow(() ->
			new GlobalException(ErrorCode.DASHBOARD_TEAM_NOT_FOUND));

		List<User> members = memberService.getMembersByTeamId(team.getId());

		int teamProgress = (int)members.stream().mapToDouble(member -> {
			List<Task> tasks = taskRepository.findByUser(member);
			int total = tasks.size();
			long completed = tasks.stream().filter(t -> t.getTaskStatus() == TaskStatus.COMPLETED).count();
			return total == 0 ? 0 : (completed * 100.0 / total);
		}).average().orElse(0);

		List<Task> taskAll = taskRepository.findAll();
		int totalTask = taskAll.size();
		long completed = taskAll.stream().filter(t -> t.getTaskStatus() == TaskStatus.COMPLETED).count();
		int completionRate = totalTask == 0 ? 0 : (int)(((double)completed / totalTask) * 100);

		return DashboardResponse.of(dashboardStats.getTotalTasks(), dashboardStats.getCompletedTasks(),
			dashboardStats.getInProgressTasks(), dashboardStats.getTodoTasks(), dashboardStats.getOverDueTasks(),
			teamProgress, dashboardStats.getMyTasksToday(), completionRate);
	}
}
