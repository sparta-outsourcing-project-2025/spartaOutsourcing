package com.example.spartaoutsourcing.domain.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spartaoutsourcing.domain.task.entity.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE (t.title LIKE %:keyword%) OR (t.description LIKE %:keyword%)")
    List<Task> findTasksByKeyword(@Param("keyword") String keyword);
}
