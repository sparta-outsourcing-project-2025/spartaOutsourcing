package com.example.spartaoutsourcing.domain.task.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spartaoutsourcing.domain.task.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

	Optional<Task> findByIdAndUserId(Long taskId, Long userId);
}
