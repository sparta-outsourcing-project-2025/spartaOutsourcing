package com.example.spartaoutsourcing.domain.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.spartaoutsourcing.domain.task.dto.response.TaskProjection;
import com.example.spartaoutsourcing.domain.task.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

	@Query(value = """
		SELECT t.id AS id, t.title AS title, t.description AS description,
		t.due_date AS dueDate, t.task_priority AS taskPriority, t.task_status AS taskStatus,
		u.id AS assigneeId, u.username AS username, u.name AS name, u.email AS email,
		t.created_at AS createdAt, t.modified_at AS updatedAt
		FROM tasks t 
		LEFT JOIN users u ON u.id = t.user_id
		WHERE (:status IS NULL OR t.task_status = :status) AND
		(:search IS NULL OR t.description LIKE %:search%) AND
		(:assigneeId IS NULL OR u.id = :assigneeId)
		LIMIT :size OFFSET :offset;
	""", nativeQuery = true)
	List<TaskProjection> findAll(
		@Param("status") String status,
		@Param("search") String search,
		@Param("assigneeId") Long assigneeId,
		@Param("offset") Long offset,
		@Param("size") Long size);

	@Query(value = """
		select count(*) from tasks
	""", nativeQuery = true)
	Long countTasksAll();
}
