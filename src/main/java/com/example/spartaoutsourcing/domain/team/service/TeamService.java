package com.example.spartaoutsourcing.domain.team.service;

import com.example.spartaoutsourcing.domain.member.dto.MemberResponse;
import com.example.spartaoutsourcing.domain.member.entity.Member;
import com.example.spartaoutsourcing.domain.team.dto.request.TeamRequest;
import com.example.spartaoutsourcing.domain.team.dto.response.TeamResponse;
import com.example.spartaoutsourcing.domain.team.entity.Team;
import com.example.spartaoutsourcing.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional
    public TeamResponse save(TeamRequest teamRequest) {

        List<Member> member = new ArrayList<>();
        List<MemberResponse> members = new ArrayList<>();
        Team team = Team.of(teamRequest.getName(), teamRequest.getDescription(), member);
        teamRepository.save(team);

        return TeamResponse.from(team);
    }

    @Transactional(readOnly = true)
    public List<TeamResponse> getTeams(){

        return teamRepository.findAll().stream()
                .map(TeamResponse::from)
                .toList();
    }
}
