package com.example.spartaoutsourcing.domain.member.dto.response;


import com.example.spartaoutsourcing.domain.member.entity.Member;
import com.example.spartaoutsourcing.domain.user.entity.User;
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
    private LocalDateTime createdAt;

    public static MemberResponse from(Member member) {
        User user = member.getUser();
        if (user == null || user.getDeletedAt() != null) return null;
        return new MemberResponse(
                member.getUser().getId(),
                member.getUser().getUsername(),
                member.getUser().getName(),
                member.getUser().getEmail(),
                member.getUser().getRole(),
                member.getUser().getCreatedAt()
        );
    }
}