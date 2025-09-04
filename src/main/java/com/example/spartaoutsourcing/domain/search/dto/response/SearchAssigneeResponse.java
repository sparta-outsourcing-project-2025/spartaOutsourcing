package com.example.spartaoutsourcing.domain.search.dto.response;

import com.example.spartaoutsourcing.domain.task.enums.TaskStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchAssigneeResponse {
    private final Long id;
    private final String name;

    public static SearchAssigneeResponse of(Long id, String name) {
        return new SearchAssigneeResponse(id, name);
    }
}
