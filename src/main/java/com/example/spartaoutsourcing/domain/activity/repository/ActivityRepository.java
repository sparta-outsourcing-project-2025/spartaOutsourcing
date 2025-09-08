package com.example.spartaoutsourcing.domain.activity.repository;

import com.example.spartaoutsourcing.domain.activity.entity.Activity;
import com.example.spartaoutsourcing.domain.activity.enums.ActivityType;
import com.example.spartaoutsourcing.domain.dashboard.dto.response.DashboardActivitiesProjection;
import com.example.spartaoutsourcing.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity,Integer> {
    @Query(value = """
		SELECT a.id, a.created_at, a.description, 
		a.type AS action, a.task_id AS targetId, a.user_id, 
		u.id, u.name,
		CASE WHEN a.type LIKE '%TASK%' THEN 'task'
  		WHEN a.type LIKE '%COMMENT%' THEN 'comment'
  		END AS targetType 
		FROM activities a
  		INNER JOIN users u ON u.id = a.user_id
  		LIMIT :size OFFSET :offset;
	""", nativeQuery = true)
    List<DashboardActivitiesProjection> findActivityAll(@Param("offset") Long offset, @Param("size") Long size);

    @Query(value = """
        SELECT COUNT(*) FROM activities
	""", nativeQuery = true)
    Long countActivityAll();

    @Query("""
                SELECT a
                FROM Activity a
                WHERE
                    (a.type = :type OR :type IS NULL) AND
                    (a.user = :user OR :user IS NULL) AND
                    (a.taskId = :task_id OR :task_id IS NULL) AND
                    (a.createdAt >= :start_date OR :start_date IS NULL) AND
                    (a.createdAt <= :end_date OR :end_date IS NULL)
           """)
    Page<Activity> findByTypeAndUserIdAndTaskIdAndCreatedAtBetween(
            @Param("type") ActivityType type,
            @Param("user") User user,
            @Param("task_id") Long taskId,
            @Param("start_date") LocalDateTime startDate,
            @Param("end_date") LocalDateTime endDate,
            Pageable pageable);
}
