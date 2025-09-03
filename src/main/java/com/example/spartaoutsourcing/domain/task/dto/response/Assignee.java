package com.example.spartaoutsourcing.domain.task.dto.response;

import lombok.Getter;

@Getter
public class Assignee {

	private Long id;
	private String username;
	private String name;
	private String email;

	public Assignee(Long id, String username, String name, String email) {
		this.id = id;
		this.username = username;
		this.name = name;
		this.email = email;
	}

	public static Assignee of(Long id, String username, String name, String email) {
		return new Assignee(id, username, name, email);
	}
}
