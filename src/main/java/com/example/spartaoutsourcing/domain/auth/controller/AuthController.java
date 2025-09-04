package com.example.spartaoutsourcing.domain.auth.controller;

import com.example.spartaoutsourcing.common.consts.SuccessCode;
import com.example.spartaoutsourcing.common.dto.GlobalApiResponse;
import com.example.spartaoutsourcing.domain.auth.dto.request.LoginRequest;
import com.example.spartaoutsourcing.domain.auth.dto.response.LoginResponse;
import com.example.spartaoutsourcing.domain.auth.dto.request.RegisterRequest;
import com.example.spartaoutsourcing.domain.auth.dto.response.RegisterResponse;
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
    public GlobalApiResponse<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        RegisterResponse response = authService.register(registerRequest);
        return GlobalApiResponse.of(SuccessCode.CREATED, response);
    }

    @PostMapping("/api/auth/login")
    public GlobalApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return GlobalApiResponse.of(SuccessCode.SUCCESS_LOGIN, response);
    }
}