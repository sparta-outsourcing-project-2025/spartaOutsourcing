package com.example.spartaoutsourcing.domain.task.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Assignee {

	private Long id;
	private String username;
	private String name;
	private String email;

	public static Assignee of(Long id, String username, String name, String email) {
		return new Assignee(id, username, name, email);
	}
}
