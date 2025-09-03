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
     * 사용자 요청을 아이디로 조회
     *
     * @param userId 사용자 아이디
     * @return 사용자 응답 반환
     */
    @Transactional(readOnly = true)
    public UserResponse getUserResponseById(Long userId) {
        User user =  userRepository.findById(userId).orElseThrow(
                ()-> new GlobalException(ErrorCode.USER_NOT_FOUND)
        );

        return UserResponse.from(user);
    }

    /**
     * 사용자 엔티티를 아이디로 조회
     *
     * @param userId 사용자 아이디
     * @return 사용자 엔티티
     */
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {

        return userRepository.findById(userId).orElseThrow(
                ()-> new GlobalException(ErrorCode.USER_NOT_FOUND)
        );
    }

    /**
     * 사용자 엔티티를 닉네임으로 조회
     * @param username 사용자 닉네임
     * @return 사용자 엔티티
     */
    @Transactional(readOnly = true)
    public User getUserByUserName(String username) {

        return userRepository.findByUsername(username).orElseThrow(
                ()-> new GlobalException(ErrorCode.USER_NOT_FOUND)
        );
    }

    /**
     * 사용자가 존재하는지 닉네임으로 조회
     * @param username 사용자 닉네임
     * @return 사용자가 존재하면 true, 존재하지 않으면 false
     */
    @Transactional(readOnly = true)
    public boolean existsUserByUsername(String username) {
        return userRepository.existsByUsername(username);
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
