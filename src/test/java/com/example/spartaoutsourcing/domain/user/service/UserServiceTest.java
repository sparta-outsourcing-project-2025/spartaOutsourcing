package com.example.spartaoutsourcing.domain.user.service;

import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.user.dto.response.UserResponse;
import com.example.spartaoutsourcing.domain.user.entity.User;
import com.example.spartaoutsourcing.domain.user.enums.UserRole;
import com.example.spartaoutsourcing.domain.user.repository.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("사용자 아이디로 조회 성공시 리스폰스 반환")
    void testGetUserResponseByUserId_Success() {
        // given
        Long userId = 1L;
        String username = "username";
        String email = "email@email.com";
        String name = "name";
        UserRole role = UserRole.USER;
        User user = User.of(username, email, "password", name, role);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        UserResponse response = userService.getUserResponseById(userId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo(username);
        assertThat(response.getEmail()).isEqualTo(email);
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getRole()).isEqualTo(role.toString());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("사용자 아이디로 리스폰스 조회 실패시 사용자를 찾을 수 없음 예외 반환 ")
    void testGetUserResponseByUserId_ThrowExceptionWhenUserNotFound() {
        // given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when, then
        GlobalException exception = assertThrows(GlobalException.class, () -> userService.getUserResponseById(userId));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("사용자 아이디로 조회 성공시 엔티티 반환")
    void testGetUserByUserId_Success() {
        // given
        Long userId = 1L;
        String username = "username";
        String email = "email@email.com";
        String name = "name";
        String password = "password";
        UserRole role = UserRole.USER;
        User user = User.of(username, email, password, name, role);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        User getuser = userService.getUserById(userId);

        // then
        assertThat(getuser).isNotNull();
        assertThat(getuser.getUsername()).isEqualTo(username);
        assertThat(getuser.getEmail()).isEqualTo(email);
        assertThat(getuser.getName()).isEqualTo(name);
        assertThat(getuser.getPassword()).isEqualTo(password);
        assertThat(getuser.getRole()).isEqualTo(role);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("사용자 아이디로 엔티티 조회 실패시 사용자를 찾을 수 없음 예외 반환 ")
    void testGetUserByUserId_ThrowExceptionWhenUserNotFound() {
        // given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when, then
        GlobalException exception = assertThrows(GlobalException.class, () -> userService.getUserById(userId));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("사용자 저장 후 엔티티를 반환")
    void testSave_Success() {
        // given
        Long id = 1L;
        String username = "username";
        String email = "email@email.com";
        String name = "name";
        String password = "password";
        UserRole role = UserRole.USER;
        User userToSave = User.of(username, email, password, name, role);

        User savedUserInDb = User.of(username, email, password, name, role);
        ReflectionTestUtils.setField(savedUserInDb, "id", id);

        when(userRepository.save(userToSave)).thenReturn(savedUserInDb);

        // when
        User savedUser = userService.save(userToSave);

        // then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo(username);
        assertThat(savedUser.getEmail()).isEqualTo(email);
        assertThat(savedUser.getName()).isEqualTo(name);
        assertThat(savedUser.getPassword()).isEqualTo(password);
        assertThat(savedUser.getRole()).isEqualTo(role);
    }

    @Test
    @DisplayName("사용자 닉네임으로 엔티티 조회 실패시 로그인 체크 예외 반환 ")
    void testGetUserByUsername_ThrowExceptionWhenLoginChecked() {
        // given
        String username = "username";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // when, then
        GlobalException exception = assertThrows(GlobalException.class, () -> userService.getUserByUsername(username));
        assertEquals(ErrorCode.LOGIN_CHECKED, exception.getErrorCode());
    }

    @Test
    @DisplayName("사용자 이름과 이메일이 담긴 키워드로 유저 엔티티 리스트 조회")
    void testGetUsersByKeyword_Success()
    {
        List<User> users = List.of(
                User.of("username", "email@email.com", "password", "name", UserRole.USER)
        );
        Mockito.when(userRepository.findUsersByKeyword("email@email.com"))
                .thenReturn(users);

        // when
        List<User> result = userService.getUsersByKeyword("email@email.com");

        // then
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("name", result.get(0).getName());
    }

    @Test
    @DisplayName("전체 사용자 조회")
    void testGetUsers_Success()
    {
        List<User> users = List.of(
                User.of("username", "email@email.com", "password", "name", UserRole.USER)
        );

        Mockito.when(userRepository.findAll()).thenReturn(users);

        // when
        List<User> result = userService.getUsers();

        // then
        Assertions.assertEquals(1, result.size());
    }
}
