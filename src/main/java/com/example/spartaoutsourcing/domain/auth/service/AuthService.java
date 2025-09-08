package com.example.spartaoutsourcing.domain.auth.service;

import com.example.spartaoutsourcing.common.config.JwtUtil;
import com.example.spartaoutsourcing.common.config.PasswordEncoder;
import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.dto.AuthUserRequest;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.auth.dto.request.LoginRequest;
import com.example.spartaoutsourcing.domain.auth.dto.request.WithdrawRequest;
import com.example.spartaoutsourcing.domain.auth.dto.response.LoginResponse;
import com.example.spartaoutsourcing.domain.auth.dto.request.RegisterRequest;
import com.example.spartaoutsourcing.domain.auth.dto.response.RegisterResponse;
import com.example.spartaoutsourcing.domain.member.repository.MemberRepository;
import com.example.spartaoutsourcing.domain.member.service.MemberService;
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
    private final MemberService memberService;

    @Transactional
    public RegisterResponse register(RegisterRequest registerRequest) {

        if (userService.existsUserByUsername(registerRequest.getUsername())) {
            throw new GlobalException(ErrorCode.USERNAME_DUPLICATED);
        }
        if (userService.existsUserByEmail(registerRequest.getEmail())) {
            throw new GlobalException(ErrorCode.EMAIL_DUPLICATED);
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
        User user = userService.getUserByUsername(loginRequest.getUsername());

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()) || !userService.existsUserByUsername(loginRequest.getUsername())) {
            throw new GlobalException(ErrorCode.LOGIN_CHECKED);
        }
        String token = jwtUtil.createToken(user.getId(), user.getUsername(), user.getRole());
        return new LoginResponse(token);
    }

    /**
     * 사용자 엔티티를 아이디로 조회
     * @param authUser 로그인 확인
     * @param request 입력받은 비밀번호
     * @return 사용자 비밀번호
     */
    @Transactional
    public User withdrawUser(AuthUserRequest authUser, WithdrawRequest request) {
        User user = userService.getUserById(authUser.getId());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new GlobalException(ErrorCode.DELETE_USER);
        }

        user.softDelete();

        memberService.removeMemberByUser(user);

        return user;
    }
}
