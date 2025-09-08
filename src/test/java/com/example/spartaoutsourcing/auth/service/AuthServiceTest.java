package com.example.spartaoutsourcing.auth.service;

import com.example.spartaoutsourcing.common.config.JwtUtil;
import com.example.spartaoutsourcing.common.config.PasswordEncoder;
import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.auth.dto.request.LoginRequest;
import com.example.spartaoutsourcing.domain.auth.dto.request.RegisterRequest;
import com.example.spartaoutsourcing.domain.auth.dto.response.LoginResponse;
import com.example.spartaoutsourcing.domain.auth.service.AuthService;
import com.example.spartaoutsourcing.domain.user.entity.User;
import com.example.spartaoutsourcing.domain.user.enums.UserRole;
import com.example.spartaoutsourcing.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private AuthService authService;

    @Test
    public void username이_이미_사용중인_아이디일때_예외처리() {
        // given
        String username = "testUser";
        String email = "testUser@email.com";
        RegisterRequest request = new RegisterRequest();
        ReflectionTestUtils.setField(request, "username", username);
        ReflectionTestUtils.setField(request, "email", email);

        when(userService.existsUserByUsername(username)).thenReturn(true);

        // when & then
        GlobalException exception = assertThrows(GlobalException.class, () -> authService.register(request));
        assertEquals(ErrorCode.USERNAME_DUPLICATED, exception.getErrorCode());
    }

    @Test
    void register_이미_사용중인_이메일_예외() {
        // given
        String username = "testUser";
        String email = "testUser@email.com";
        RegisterRequest request = new RegisterRequest();
        ReflectionTestUtils.setField(request, "username", username);
        ReflectionTestUtils.setField(request, "email", email);

        when(userService.existsUserByUsername(username)).thenReturn(false);
        when(userService.existsUserByEmail(email)).thenReturn(true);

        // when & then
        GlobalException exception = assertThrows(GlobalException.class, () -> authService.register(request));
        assertEquals(ErrorCode.EMAIL_DUPLICATED, exception.getErrorCode());
    }

    @Test
    void 로그인_할_때_잘못된_패스워드는_false를_반환한다() {
        // given
        String rawPassword = "testPassword";
        String wrongPassword = "wrongPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // when
        boolean matches = passwordEncoder.matches(wrongPassword, encodedPassword);

        // then
        assertFalse(matches, "잘못된 패스워드는 matches 메서드에서 false를 반환해야 함");
    }

    @Test
    void login_성공() {
        // given
        String username = "testUser";
        String email = "test@email.com";
        String rawPassword = "password";
        String name = "testName";
        String encodedPassword = "encodedPassword";

        LoginRequest request = new LoginRequest();
        ReflectionTestUtils.setField(request, "username", username);
        ReflectionTestUtils.setField(request, "password", rawPassword);

        User user = User.of(
                username,
                email,
                encodedPassword,
                name,
                UserRole.USER
        );
        ReflectionTestUtils.setField(user, "id", 1L);

        // Mocking
        when(userService.getUserByUsername(username)).thenReturn(user);
        when(userService.existsUserByUsername(username)).thenReturn(true);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(jwtUtil.createToken(1L, username, UserRole.USER)).thenReturn("mockToken");

        // when
        LoginResponse response = authService.login(request);

        // then
        assertEquals("mockToken", response.getToken());
    }
}
