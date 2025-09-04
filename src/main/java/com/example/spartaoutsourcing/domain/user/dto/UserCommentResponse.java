package com.example.spartaoutsourcing.domain.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCommentResponse {
    private final Long id;
    private final String username;
    private final String name;
    private final String email;
    private final String role;

    public static UserCommentResponse of(Long id, String username, String name, String email, String role) {
        return new UserCommentResponse(id, username, name, email, role);
    }
}
