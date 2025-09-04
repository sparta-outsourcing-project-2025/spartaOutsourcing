package com.example.spartaoutsourcing.domain.team.service;

import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.domain.member.dto.MemberResponse;
import com.example.spartaoutsourcing.domain.team.dto.request.TeamRequest;
import com.example.spartaoutsourcing.domain.team.dto.response.TeamResponse;
import com.example.spartaoutsourcing.domain.team.entity.Team;
import com.example.spartaoutsourcing.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional
    public TeamResponse save(TeamRequest teamRequest) {
        if (teamRepository.existsByName(teamRequest.getName())) {
            throw new GlobalException(ErrorCode.TEAM_NAME_DUPLICATED);
        }

        Team team = Team.of(teamRequest.getName(), teamRequest.getDescription(), new ArrayList<>());
        teamRepository.save(team);
        return TeamResponse.from(team);
    }

    @Transactional(readOnly = true)
    public List<TeamResponse> getTeams() {
        return teamRepository.findAll().stream()
                .map(TeamResponse::from)
                .toList();
    }

    @Transactional
    public void delete(Long teamId){
        Team team = teamRepository.findById(teamId).get();
        teamRepository.delete(team);
    }
                Collections.emptyList()
        );
    }
}
