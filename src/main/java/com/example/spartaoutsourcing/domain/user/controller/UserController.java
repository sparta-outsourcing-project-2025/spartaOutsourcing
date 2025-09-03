package com.example.spartaoutsourcing.domain.user.controller;

import com.example.spartaoutsourcing.common.annotation.Auth;
import com.example.spartaoutsourcing.common.consts.SuccessCode;
import com.example.spartaoutsourcing.common.dto.AuthUserRequest;
import com.example.spartaoutsourcing.common.dto.GlobalApiResponse;
import com.example.spartaoutsourcing.domain.user.dto.UserResponse;
import com.example.spartaoutsourcing.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 현재 사용자 정보 조회
     * @param authUser 로그인 중인 사용자 요청
     * @return 공통 응답과 사용자 응답 반환
     */
    @GetMapping("/me")
    public GlobalApiResponse<UserResponse> getUser(@Auth AuthUserRequest authUser) {
        return GlobalApiResponse.of(SuccessCode.SUCCESS_GET_USER,userService.getUserById(authUser.getId()));
    }
}
