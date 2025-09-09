package com.example.spartaoutsourcing.domain.search.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AssigneeSearchResponse {
    private final Long id;
    private final String name;

    public static AssigneeSearchResponse of(Long id, String name) {
        return new AssigneeSearchResponse(id, name);
    }
}
