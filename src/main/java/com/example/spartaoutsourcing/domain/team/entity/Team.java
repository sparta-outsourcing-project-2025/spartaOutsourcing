package com.example.spartaoutsourcing.domain.team.entity;

import com.example.spartaoutsourcing.common.entity.AuditableEntity;
import com.example.spartaoutsourcing.domain.member.dto.MemberResponse;
import com.example.spartaoutsourcing.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "teams")
@NoArgsConstructor
public class Team extends AuditableEntity {

    @Column(nullable = false)
    private String name;
    private String description;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Member> members = new ArrayList<>();

    public static Team of(String name, String description, List<Member> members) {
        return new Team(name, description, members);
    }
}
