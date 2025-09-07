package com.example.spartaoutsourcing.domain.user.service;

import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.user.dto.response.UserResponse;
import com.example.spartaoutsourcing.domain.user.entity.User;
import com.example.spartaoutsourcing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
     * 사용자 존재하는지 이메일로 확인
     * @param email 사용자 이메일
     * @return 이메일이 존재하면 true, 존재하지 않으면 false
     */
    @Transactional(readOnly = true)
    public boolean existsUserByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    /**
     * 사용자가 존재하는지 닉네임으로 조회
     * @param username 사용자 닉네임
     * @return 사용자명이 존재하면 true, 존재하지 않으면 false
     */
    @Transactional(readOnly = true)
    public boolean existsUserByUsername(String username) {
        return userRepository.existsUserByUsername(username);
    }

    /**
     * 회원가입한 User 정보를 저장
     */
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * 사용자가 존재하는지 닉네임으로 조회
     * @param username 사용자 닉네임
     * @return 존재하면 데이터를 가져온다
     */
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new GlobalException(ErrorCode.LOGIN_CHECKED));
    }

    /**
     * {@link User}들을 이름, 이메일로 조회한다
     *
     * @param keyword 조회할 키워드
     * @return 조회된 {@link User} 리스트
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByKeyword(String keyword) {
        return userRepository.findUsersByKeyword(keyword);
    }
}
