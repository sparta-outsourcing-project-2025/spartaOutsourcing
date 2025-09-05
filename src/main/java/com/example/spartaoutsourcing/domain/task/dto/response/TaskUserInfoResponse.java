package com.example.spartaoutsourcing.domain.task.dto.response;

import com.example.spartaoutsourcing.domain.user.enums.UserRole;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskUserInfoResponse {
	private Long id;
	private String email;
	private String name;
	private UserRole role;

	public static TaskUserInfoResponse of(Long id, String email, String name, UserRole role) {
		return new TaskUserInfoResponse(id, email, name, role);
	}
}
