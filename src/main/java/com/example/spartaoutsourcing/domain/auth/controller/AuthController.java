package com.example.spartaoutsourcing.domain.auth.controller;

import com.example.spartaoutsourcing.domain.auth.dto.LoginRequest;
import com.example.spartaoutsourcing.domain.auth.dto.LoginResponse;
import com.example.spartaoutsourcing.domain.auth.dto.RegisterRequest;
import com.example.spartaoutsourcing.domain.auth.dto.RegisterResponse;
import com.example.spartaoutsourcing.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/auth/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/api/auth/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
}
