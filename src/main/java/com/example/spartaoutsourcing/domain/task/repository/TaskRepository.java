package com.example.spartaoutsourcing.domain.task.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.spartaoutsourcing.domain.dashboard.dto.response.DashboardMyTaskProjection;
import com.example.spartaoutsourcing.domain.dashboard.dto.response.DashboardStatsProjection;
import com.example.spartaoutsourcing.domain.task.dto.response.TaskProjection;
import com.example.spartaoutsourcing.domain.task.entity.Task;
import com.example.spartaoutsourcing.domain.user.entity.User;


public interface TaskRepository extends JpaRepository<Task, Long> {
    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT t FROM Task t WHERE (t.title LIKE %:keyword%) OR (t.description LIKE %:keyword%)")
    List<Task> findTasksByKeyword(@Param("keyword") String keyword);

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT t FROM Task t WHERE (t.title LIKE %:keyword%) OR (t.description LIKE %:keyword%)")
    Page<Task> findTaskPageByKeyword(@Param("keyword") String keyword,  Pageable pageable);

	@Query(value = """
		SELECT t.id AS id, t.title AS title, t.description AS description,
		t.due_date AS dueDate, t.task_priority AS priority, t.task_status AS status,
		u.id AS assigneeId, u.username AS username, u.name AS name, u.email AS email,
		t.created_at AS createdAt, t.modified_at AS updatedAt
		FROM tasks t 
		LEFT JOIN users u ON u.id = t.user_id
		WHERE t.deleted_at IS NULL AND 
		(:status IS NULL OR t.task_status = :status) AND
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

	@Query(value = """
		SELECT COUNT(*) AS totalTasks,
  		SUM(CASE WHEN t.task_status = 'COMPLETED' THEN 1 ELSE 0 END) AS completedTasks,
  		SUM(CASE WHEN t.task_status = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS inProgressTasks,
  		SUM(CASE WHEN t.task_status = 'TODO' THEN 1 ELSE 0 END) AS todoTasks,
  		SUM(CASE WHEN t.due_date < current_date() AND t.task_status <> 'COMPLETED' THEN 1 ELSE 0 END) AS overdueTasks,
  		SUM(CASE WHEN current_date() < t.due_date AND t.task_status <> 'COMPLETED' THEN 1 ELSE 0 END) AS myTasksToday
  		FROM tasks t
	""", nativeQuery = true)
	DashboardStatsProjection findDashboardStats();

	List<Task> findByUser(User user);

	@Query(value = """
		SELECT t.id, t.title, t.task_status AS taskStatus, t.due_date AS dueDate,
		CASE WHEN DATE(t.due_date) = current_date() THEN 'TODAY'
		WHEN DATE(t.due_date) > current_date() THEN 'UPCOMING'
  		WHEN DATE(t.due_date) < current_date() THEN 'OVERDUE'
  		END AS taskCategory
  		FROM tasks t
  		ORDER BY t.due_date ASC;
	""", nativeQuery = true)
	List<DashboardMyTaskProjection> findMyTaskAll();

	@Query("SELECT t FROM Task t WHERE (:status IS NULL OR t.taskStatus = :status) ORDER BY t.id DESC")
	List<Task> findAllByStatus(@Param("status") String status, @Param("offset") long offset, @Param("limit") long limit);

	@Query("SELECT COUNT(t) FROM Task t WHERE (:status IS NULL OR t.taskStatus = :status)")
	long countByStatus(@Param("status") String status);
}
