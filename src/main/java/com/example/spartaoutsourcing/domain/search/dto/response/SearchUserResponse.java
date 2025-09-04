package com.example.spartaoutsourcing.domain.search.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchUserResponse {
    private final Long id;
    private final String username;
    private final String name;
    private final String email;

    public static SearchUserResponse of(Long id, String username, String name, String email) {
        return new SearchUserResponse(id, username, name, email);
    }
}
