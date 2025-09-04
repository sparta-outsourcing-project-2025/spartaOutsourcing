package com.example.spartaoutsourcing.domain.user.entity;

import com.example.spartaoutsourcing.common.entity.AuditableEntity;
import com.example.spartaoutsourcing.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Getter
@Entity
@Table(name="users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access =  AccessLevel.PRIVATE)
// 조회 시 삭제된 메시지는 보이지 않음
@Where(clause = "deleted = false")
public class User extends AuditableEntity {
    @Column(nullable = false, length = 20)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    private boolean deleted = false;

    private User (String username, String email, String password, String name, UserRole role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public static User of(String username, String email, String password, String name, UserRole role)
    {
        return new User(username,email, password, name, role);
    }

    public void softDelete() {
        this.deleted = true;
    }
}
