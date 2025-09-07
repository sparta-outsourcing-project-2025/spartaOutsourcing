package com.example.spartaoutsourcing.domain.activity.repository;

import java.util.List;

import com.example.spartaoutsourcing.domain.activity.entity.Activity;
import com.example.spartaoutsourcing.domain.dashboard.dto.response.DashboardActivitiesProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}
