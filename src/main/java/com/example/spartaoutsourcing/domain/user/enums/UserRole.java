package com.example.spartaoutsourcing.domain.user.enums;

public enum UserRole {
    USER, ADMIN;

    public static UserRole of(String value) {
        return UserRole.valueOf(value);
    }
}
