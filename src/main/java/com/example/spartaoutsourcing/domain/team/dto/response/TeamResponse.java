package com.example.spartaoutsourcing.domain.team.dto.response;

import com.example.spartaoutsourcing.domain.member.dto.MemberResponse;
import com.example.spartaoutsourcing.domain.team.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.example.spartaoutsourcing.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class TeamResponse {
    private final Long id;
    private final String name;
    private final String description;
    private final LocalDateTime createdAt;
    private final List<MemberResponse> members;


    public static TeamResponse from(Team team) {
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .createdAt(team.getCreatedAt())
                .members(
                        team.getMembers().stream()
                                .map(MemberResponse::from)
                                .toList()
                )
                .build();
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
