package com.example.spartaoutsourcing.domain.dashboard.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DashboardUserInfoResponse {
	private Long id;
	private String name;

	public static DashboardUserInfoResponse of(Long id, String name) {
		return new DashboardUserInfoResponse(id, name);
	}
}
