package com.example.spartaoutsourcing.domain.user.service;

import com.example.spartaoutsourcing.common.consts.ErrorCode;
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
     * @param userId 사용자 아이디
     * @return 사용자 응답 반환
     */
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        User user =  userRepository.findById(userId).orElseThrow(
                ()-> new GlobalException(ErrorCode.USER_NOT_FOUND)
        );

        return UserResponse.from(user);
    }
}
