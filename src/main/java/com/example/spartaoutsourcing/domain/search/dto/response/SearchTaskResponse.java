package com.example.spartaoutsourcing.domain.search.dto.response;

import com.example.spartaoutsourcing.domain.task.enums.TaskStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchTaskResponse {
    private final Long id;
    private final String title;
    private final String description;
    private final String status;
    private final SearchAssigneeResponse assignee;

    public static SearchTaskResponse of(Long id, String title, String description, TaskStatus status, SearchAssigneeResponse assignee) {
        return new SearchTaskResponse(id, title, description, status.toString(), assignee);
    }
}
