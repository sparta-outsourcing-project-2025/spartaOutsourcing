package com.example.spartaoutsourcing.domain.search.dto.response;

import com.example.spartaoutsourcing.domain.task.enums.TaskStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskSearchResponse {
    private final Long id;
    private final String title;
    private final String description;
    private final TaskStatus status;
    private final AssigneeSearchResponse assignee;

    public static TaskSearchResponse of(Long id, String title, String description, TaskStatus status, AssigneeSearchResponse assignee) {
        return new TaskSearchResponse(id, title, description, status, assignee);
    }
}
