package com.example.spartaoutsourcing.domain.member.dto;


import com.example.spartaoutsourcing.domain.member.entity.Member;
import com.example.spartaoutsourcing.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
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

    public static MemberResponse of(Long id, String username, String name, String email, UserRole role) {
        return new MemberResponse(id, username, name, email, role);
    }

}