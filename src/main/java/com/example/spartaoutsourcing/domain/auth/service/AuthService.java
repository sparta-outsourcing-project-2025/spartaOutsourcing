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
import com.example.spartaoutsourcing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public RegisterResponse register(RegisterRequest registerRequest) {

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new GlobalException(ErrorCode.USERNAME_DUPLICATED);
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        User userRole = User.createUser(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                encodedPassword,
                registerRequest.getName()
        );
        User savedUser = userRepository.save(userRole);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getName(),
                savedUser.getUserRole().name(),
                savedUser.getCreatedAt()
        );
    }

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new GlobalException(ErrorCode.LOGIN_CHECKED));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new GlobalException(ErrorCode.LOGIN_CHECKED);
        }
        String token = jwtUtil.createToken(user.getId(), user.getUsername());

        return new LoginResponse(token);
    }
}
