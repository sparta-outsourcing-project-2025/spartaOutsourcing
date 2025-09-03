package com.example.spartaoutsourcing.domain.auth.dto;

import lombok.Getter;

@Getter
public class LoginResponse {

    private final String token;

    public LoginResponse(String token) {
        this.token = token;
    }
}