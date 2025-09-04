package com.example.spartaoutsourcing.domain.search.dto.response;

import com.example.spartaoutsourcing.domain.task.enums.TaskPriority;
import com.example.spartaoutsourcing.domain.task.enums.TaskStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskContentSearchResponse {
    private final Long id;
    private final String title;
    private final String description;
    private final TaskStatus status;
    private final TaskPriority priority;
    private final Long assigneeId;
    private final AssigneeSearchResponse assignee;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime dueDate;

    public static TaskContentSearchResponse of(
            Long id,
            String title,
            String description,
            TaskStatus status,
            TaskPriority priority,
            Long assigneeId,
            AssigneeSearchResponse assignee,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime dueDate) {
        return new TaskContentSearchResponse(
                id,
                title,
                description,
                status,
                priority,
                assigneeId,
                assignee,
                createdAt,
                updatedAt,
                dueDate);
    }
}
