package com.example.spartaoutsourcing.domain.member.dto;


import com.example.spartaoutsourcing.domain.member.entity.Member;
import com.example.spartaoutsourcing.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class MemberResponse {
    private Long id;
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

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getUser().getUsername(),
                member.getUser().getName(),
                member.getUser().getEmail(),
                member.getUser().getRole(),
                member.getCreatedAt()
        );
    }

}
