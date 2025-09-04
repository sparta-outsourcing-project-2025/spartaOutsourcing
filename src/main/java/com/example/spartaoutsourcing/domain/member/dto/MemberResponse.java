package com.example.spartaoutsourcing.domain.member.dto;


import com.example.spartaoutsourcing.domain.member.entity.Member;
import com.example.spartaoutsourcing.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MemberResponse {
    private Long id;
    private String username;
    private String name;
    private String email;
    private UserRole role;
    private LocalDateTime createdAt; // 추가

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getUser().getUsername(),
                member.getUser().getName(),
                member.getUser().getEmail(),
                member.getUser().getRole(),
                member.getUser().getCreatedAt()
        );
    }


    public static MemberResponse of(Long id, String username, String name, String email, UserRole role, LocalDateTime createdAt) {
        return new MemberResponse(id, username, name, email, role, createdAt);
    }

}