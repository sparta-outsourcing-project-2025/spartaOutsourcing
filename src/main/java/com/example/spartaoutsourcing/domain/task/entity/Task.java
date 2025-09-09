package com.example.spartaoutsourcing.domain.task.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;

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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tasks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assignee_id")
	private User assignee;

	private Task(String title, String description, TaskStatus taskStatus, LocalDateTime dueDate, TaskPriority taskPriority, User user, User assignee) {
		this.title = title;
		this.description = description;
		this.taskStatus = taskStatus;
		this.dueDate = dueDate;
		this.taskPriority = taskPriority;
		this.user = user;
		this.assignee = assignee;
	}

	public static Task of(String title, String description, TaskStatus taskStatus, LocalDateTime dueDate,
		TaskPriority taskPriority, User user, User assignee) {
		return new Task(title, description, taskStatus, dueDate, taskPriority, user, assignee);
	}

	public void update(String title, String description, LocalDateTime dueDate, User assignee) {
		this.title = title;
		this.description = description;
		// this.taskStatus = taskStatus;
		this.dueDate = dueDate;
		// this.taskStatus = taskStatus;
		this.assignee = assignee;
	}

	public void statusUpdate(TaskStatus taskStatus) {
		this.taskStatus = taskStatus;
	}

	public void softDelete() {
		this.deletedAt = LocalDateTime.now();
	}
}
