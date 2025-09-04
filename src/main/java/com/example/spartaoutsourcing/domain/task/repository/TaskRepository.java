package com.example.spartaoutsourcing.domain.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spartaoutsourcing.domain.task.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
