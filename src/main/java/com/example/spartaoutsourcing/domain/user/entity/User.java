package com.example.spartaoutsourcing.domain.user.entity;


import com.example.spartaoutsourcing.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Column(length = 20)
    private String username;

    @Column(length = 30, unique = true)
    private String email;

    @Column(length = 100)

    private String password;

    @Column(length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    // 생성자 private으로 막기
    private User(String username, String email, String password, String name, UserRole userRole) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.userRole = userRole;
    }

    // 정적 팩토리 메서드: 일반 회원 생성
    public static User createUser(String username, String email, String password, String name) {
        return new User(username, email, password, name, UserRole.USER);
    }

    // 정적 팩토리 메서드: 관리자 생성
//    public static User createAdmin(String username, String email, String password, String name) {
//        return new User(username, email, password, name, UserRole.ADMIN);
//    }
}
