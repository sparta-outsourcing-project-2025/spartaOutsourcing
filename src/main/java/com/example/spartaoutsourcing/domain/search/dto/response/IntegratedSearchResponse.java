package com.example.spartaoutsourcing.domain.search.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IntegratedSearchResponse {
    private final List<TaskSearchResponse> tasks;
    private final List<UserSearchResponse> users;
    private final List<TeamSearchResponse> teams;

    public static IntegratedSearchResponse of(List<TaskSearchResponse> tasks, List<UserSearchResponse> users, List<TeamSearchResponse> teams) {
        return new IntegratedSearchResponse(tasks, users, teams);
    }
}
