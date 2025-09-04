package com.example.spartaoutsourcing.domain.search.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamSearchResponse {
    private final Long id;
    private final String name;
    private final String description;

    public static TeamSearchResponse of(Long id, String name, String description) {
        return new TeamSearchResponse(id, name, description);
    }
}
