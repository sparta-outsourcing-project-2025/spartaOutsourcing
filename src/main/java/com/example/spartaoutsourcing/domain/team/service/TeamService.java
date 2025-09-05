package com.example.spartaoutsourcing.domain.team.service;

import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.member.dto.response.MemberResponse;
import com.example.spartaoutsourcing.domain.member.entity.Member;
import com.example.spartaoutsourcing.domain.member.repository.MemberRepository;
import com.example.spartaoutsourcing.domain.team.dto.request.TeamRequest;
import com.example.spartaoutsourcing.domain.team.dto.response.TeamResponse;
import com.example.spartaoutsourcing.domain.team.entity.Team;
import com.example.spartaoutsourcing.domain.team.repository.TeamRepository;
import com.example.spartaoutsourcing.domain.user.entity.User;
import com.example.spartaoutsourcing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public TeamResponse save(TeamRequest teamRequest) {
        if (teamRepository.existsByName(teamRequest.getName())) {
            throw new GlobalException(ErrorCode.TEAM_NAME_DUPLICATED);
        }

        Team team = Team.of(teamRequest.getName(), teamRequest.getDescription());
        teamRepository.save(team);

        return TeamResponse.of(
                team.getId(),
                team.getName(),
                team.getDescription(),
                team.getCreatedAt(),
                Collections.emptyList()
        );
    }

    @Transactional(readOnly = true)
    public List<TeamResponse> getTeams() {
        return teamRepository.findAll().stream()
                .map(team -> TeamResponse.of(
                        team.getId(),
                        team.getName(),
                        team.getDescription(),
                        team.getCreatedAt(),
                        team.getMembers().stream()
                                .filter(member -> member.getUser() != null)
                                .map(MemberResponse::from)
                                .toList()
                ))
                .toList();
    }

    @Transactional
    public TeamResponse updateTeam(Long teamId, TeamRequest teamRequest) {
        Team team = teamRepository.findById(teamId).orElse(null);

        team.updateInfo(teamRequest.getName(), teamRequest.getDescription());

        return TeamResponse.from(team);
    }

    @Transactional
    public void delete(Long teamId){
        Team team = teamRepository.findById(teamId).get();
        teamRepository.delete(team);
    }

    /**
     * {@link Team}들을 이름, 내용으로 조회한다
     *
     * @param keyword 조회할 키워드
     * @return 조회된 {@link Team} 리스트
     */
    @Transactional(readOnly = true)
    public List<Team> getTeamsByKeyword(String keyword) {
        return teamRepository.findTeamsByKeyword(keyword);
    }

    @Transactional
    public void delete(Long teamId){
        Team team = teamRepository.findById(teamId).get();
        teamRepository.delete(team);
    }

    @Transactional
    public TeamResponse updateTeam(Long teamId, TeamRequest teamRequest) {
        Team team = teamRepository.findById(teamId).get();

        team.updateInfo(teamRequest.getName(), teamRequest.getDescription());

        return TeamResponse.from(team);
    }
}
