package com.example.spartaoutsourcing.domain.auth.controller;


import com.example.spartaoutsourcing.common.annotation.Auth;
import com.example.spartaoutsourcing.common.consts.SuccessCode;
import com.example.spartaoutsourcing.common.dto.AuthUserRequest;
import com.example.spartaoutsourcing.common.dto.GlobalApiResponse;
import com.example.spartaoutsourcing.domain.auth.dto.request.LoginRequest;
import com.example.spartaoutsourcing.domain.auth.dto.request.WithdrawRequest;
import com.example.spartaoutsourcing.domain.auth.dto.response.LoginResponse;
import com.example.spartaoutsourcing.domain.auth.dto.request.RegisterRequest;
import com.example.spartaoutsourcing.domain.auth.dto.response.RegisterResponse;
import com.example.spartaoutsourcing.domain.auth.service.AuthService;

import com.example.spartaoutsourcing.domain.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public GlobalApiResponse<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        RegisterResponse response = authService.register(registerRequest);
        return GlobalApiResponse.of(SuccessCode.CREATED, response);
    }

    @PostMapping("/login")
    public GlobalApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return GlobalApiResponse.of(SuccessCode.SUCCESS_LOGIN, response);
    }

    @DeleteMapping("/withdraw")
    public GlobalApiResponse<UserResponse> deleteUser(
            @Auth AuthUserRequest authUser,
            @RequestBody @Valid WithdrawRequest withdrawRequest
    ) {
        UserResponse response = authService.withdrawUser(authUser, withdrawRequest.getPassword());
        return GlobalApiResponse.of(SuccessCode.SUCCESS_DELETE_USER, response);
    }
}