package com.example.spartaoutsourcing.domain.auth.service;

import com.example.spartaoutsourcing.common.config.JwtUtil;
import com.example.spartaoutsourcing.common.config.PasswordEncoder;
import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.auth.dto.LoginRequest;
import com.example.spartaoutsourcing.domain.auth.dto.LoginResponse;
import com.example.spartaoutsourcing.domain.auth.dto.RegisterRequest;
import com.example.spartaoutsourcing.domain.auth.dto.RegisterResponse;
import com.example.spartaoutsourcing.domain.user.entity.User;
import com.example.spartaoutsourcing.domain.user.enums.UserRole;
import com.example.spartaoutsourcing.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Transactional
    public RegisterResponse register(RegisterRequest registerRequest) {

        if (userService.existsUserByUsername(registerRequest.getUsername())) {
            throw new GlobalException(ErrorCode.USERNAME_DUPLICATED);
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        User userRole = User.of(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                encodedPassword,
                registerRequest.getName(),
                UserRole.USER
        );
        User savedUser = userService.save(userRole);

        return RegisterResponse.of(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getName(),
                savedUser.getRole().name(),
                savedUser.getCreatedAt()
        );
    }

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userService.findByUsername(loginRequest.getUsername());

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()) || !userService.existsUserByUsername(loginRequest.getUsername())) {
            throw new GlobalException(ErrorCode.LOGIN_CHECKED);
        }
        String token = jwtUtil.createToken(user.getId(), user.getUsername(), user.getRole());

        return new LoginResponse(token);
    }
}
