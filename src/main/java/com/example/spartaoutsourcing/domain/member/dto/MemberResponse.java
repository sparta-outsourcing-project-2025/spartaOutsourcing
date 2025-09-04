package com.example.spartaoutsourcing.domain.member.dto;


import com.example.spartaoutsourcing.domain.member.entity.Member;
import com.example.spartaoutsourcing.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class MemberResponse {
    private Long Id;
    private String username;
    private String name;
    private String email;
    private UserRole role;
    private LocalDateTime createdAt;


    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .username(member.getUser().getUsername())
                .name(member.getUser().getName())
                .email(member.getUser().getEmail())
                .role(member.getUser().getRole())
                .createdAt(member.getCreatedAt())
                .build();
    }

    public static MemberResponse of(Long id, String username, String name, String email, UserRole role) {
        return new MemberResponse(id, username, name, email, role);
    }

}