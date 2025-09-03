package com.example.spartaoutsourcing.domain.task.entity;

import java.time.Instant;
import java.time.LocalDateTime;

import com.example.spartaoutsourcing.common.entity.AuditableEntity;
import com.example.spartaoutsourcing.domain.task.enums.TaskPriority;
import com.example.spartaoutsourcing.domain.task.enums.TaskStatus;
import com.example.spartaoutsourcing.domain.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tasks")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends AuditableEntity {

	private String title;

	private String description;

	@Enumerated(EnumType.STRING)
	private TaskStatus taskStatus;

	private LocalDateTime dueDate;

	@Enumerated(EnumType.STRING)
	private TaskPriority taskPriority;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	public Task(String title, String description, TaskStatus taskStatus, LocalDateTime dueDate, TaskPriority taskPriority) {
		this.title = title;
		this.description = description;
		this.taskStatus = taskStatus;
		this.dueDate = dueDate;
		this.taskPriority = taskPriority;
	}

	public static Task of(String title, String description, TaskStatus taskStatus, LocalDateTime dueDate,
		TaskPriority taskPriority, User user) {
		return new Task(title, description, taskStatus, dueDate, taskPriority, user);
	}
}
