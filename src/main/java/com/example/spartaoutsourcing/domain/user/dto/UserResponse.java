package com.example.spartaoutsourcing.domain.user.dto;

import com.example.spartaoutsourcing.domain.user.entity.User;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class UserResponse {
    private final Long id;
    private final String username;
    private final String email;
    private final String name;
    private final String role;
    private final LocalDateTime createdAt;

    private UserResponse(Long id, String username, String email, String name, String role, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.role = role;
        this.createdAt = createdAt;
    }

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getName(),
                user.getRole().toString(),
                user.getCreatedAt()
        );
    }


}
