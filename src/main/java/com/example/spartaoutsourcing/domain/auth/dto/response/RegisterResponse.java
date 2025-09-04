package com.example.spartaoutsourcing.domain.auth.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RegisterResponse {

    private Long id;
    private String username;
    private String email;
    private String name;
    private String role;
    private LocalDateTime createdAt;

    public RegisterResponse(Long id, String username, String email, String name, String role, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.role = role;
        this.createdAt = createdAt;
    }

    public static RegisterResponse of(Long id, String username, String email, String name, String role, LocalDateTime createdAt) {
        return new RegisterResponse(id, username, email, name, role, createdAt);

    }
}
