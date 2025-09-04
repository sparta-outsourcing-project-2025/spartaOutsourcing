package com.example.spartaoutsourcing.domain.team.dto.response;

import com.example.spartaoutsourcing.domain.member.dto.MemberResponse;
import com.example.spartaoutsourcing.domain.team.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class TeamResponse {
    private final Long id;
    private final String name;
    private final String description;
    private final LocalDateTime createdAt;
    private final List<MemberResponse> members;


    public static TeamResponse from(Team team) {
        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getDescription(),
                team.getCreatedAt(),
                team.getMembers().stream()
                        .map(MemberResponse::from)
                        .toList()
        );
    }

    public static TeamResponse of(
            Long id,
            String name,
            String description,
            LocalDateTime createdAt,
            List<MemberResponse> members
    ){
        return new TeamResponse(id, name, description, createdAt, members);
    }
}
