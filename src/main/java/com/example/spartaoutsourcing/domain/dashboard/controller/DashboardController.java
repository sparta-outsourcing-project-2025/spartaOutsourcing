package com.example.spartaoutsourcing.domain.dashboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spartaoutsourcing.common.annotation.Auth;
import com.example.spartaoutsourcing.common.consts.SuccessCode;
import com.example.spartaoutsourcing.common.dto.AuthUserRequest;
import com.example.spartaoutsourcing.common.dto.GlobalApiResponse;
import com.example.spartaoutsourcing.domain.dashboard.dto.response.DashboardMyTaskResponse;
import com.example.spartaoutsourcing.domain.dashboard.dto.response.DashboardResponse;
import com.example.spartaoutsourcing.domain.dashboard.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

	private final DashboardService dashboardService;

	@GetMapping("/stats")
	public GlobalApiResponse<DashboardResponse> getDashboardStats(@Auth AuthUserRequest authUserRequest) {
		DashboardResponse dashboardStats = dashboardService.getDashboardStats();

		return GlobalApiResponse.of(SuccessCode.SUCCESS_FIND_DASHBOARD_STATS, dashboardStats);
	}

	@GetMapping("/my-tasks")
	public GlobalApiResponse<DashboardMyTaskResponse> getDashboardMyTasks(@Auth AuthUserRequest authUserRequest) {
		DashboardMyTaskResponse dashboardMyTasks = dashboardService.getDashboardMyTasks();

		return GlobalApiResponse.of(SuccessCode.SUCCESS_DASHBOARD_MY_TASKS, dashboardMyTasks);
	}
}
