package com.example.spartaoutsourcing.common.dto;

import com.example.spartaoutsourcing.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthUserRequest {
    private final Long id;
    private final String email;
    private final UserRole role;

    public static AuthUserRequest of(Long id, String email, UserRole role) {
        return new AuthUserRequest(id, email, role);
    }
}
