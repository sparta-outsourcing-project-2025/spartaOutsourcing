package com.example.spartaoutsourcing.domain.search.dto.response;

import com.example.spartaoutsourcing.domain.task.enums.TaskStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchTeamResponse {
    private final Long id;
    private final String name;
    private final String description;

    public static SearchTeamResponse of(Long id, String name, String description) {
        return new SearchTeamResponse(id, name, description);
    }
}
