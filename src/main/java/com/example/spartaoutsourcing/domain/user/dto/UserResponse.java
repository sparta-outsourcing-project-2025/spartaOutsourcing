package com.example.spartaoutsourcing.domain.user.dto;

import com.example.spartaoutsourcing.domain.user.entity.User;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class UserResponse {
    private final Long id;
    private final String username;
    private final String name;
    private final String email;
    private final String role;
    private final LocalDateTime createdAt;

    private UserResponse(Long id, String username, String name, String email, String role, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getRole().toString(),
                user.getCreatedAt()
        );
    }


}
