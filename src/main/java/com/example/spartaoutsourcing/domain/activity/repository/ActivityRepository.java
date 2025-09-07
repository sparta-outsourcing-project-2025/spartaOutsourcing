package com.example.spartaoutsourcing.domain.activity.repository;

import com.example.spartaoutsourcing.domain.activity.entity.Activity;
import com.example.spartaoutsourcing.domain.activity.enums.ActivityType;
import com.example.spartaoutsourcing.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ActivityRepository extends JpaRepository<Activity,Integer> {

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
