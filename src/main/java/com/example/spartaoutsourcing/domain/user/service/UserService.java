package com.example.spartaoutsourcing.domain.user.service;

import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.dto.AuthUserRequest;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.user.dto.UserResponse;
import com.example.spartaoutsourcing.domain.user.entity.User;
import com.example.spartaoutsourcing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * 사용자 조회
     *
     * @param authUser 사용자 요청
     * @return 사용자 응답 반환
     */
    @Transactional(readOnly = true)
    public UserResponse getUser(AuthUserRequest authUser) {
        User user=  userRepository.findById(authUser.getId()).orElseThrow(
                ()-> new GlobalException(ErrorCode.USER_NOT_FOUND)
        );

        return UserResponse.from(user);
    }




    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new GlobalException(ErrorCode.LOGIN_CHECKED));
    }
}
