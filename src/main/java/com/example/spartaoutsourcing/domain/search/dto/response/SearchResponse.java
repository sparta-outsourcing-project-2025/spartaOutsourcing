package com.example.spartaoutsourcing.domain.search.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchResponse {
    private final List<SearchTaskResponse> tasks;
    private final List<SearchUserResponse> users;
    private final List<SearchTeamResponse> teams;

    public static SearchResponse of(List<SearchTaskResponse> tasks, List<SearchUserResponse> users, List<SearchTeamResponse> teams) {
        return new SearchResponse(tasks, users, teams);
    }
}
