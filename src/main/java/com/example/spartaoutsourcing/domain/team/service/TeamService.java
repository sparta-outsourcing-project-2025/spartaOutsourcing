package com.example.spartaoutsourcing.domain.team.service;

import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.dto.GlobalApiResponse;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.member.dto.MemberResponse;
import com.example.spartaoutsourcing.domain.member.entity.Member;
import com.example.spartaoutsourcing.domain.team.dto.request.TeamRequest;
import com.example.spartaoutsourcing.domain.team.dto.response.TeamResponse;
import com.example.spartaoutsourcing.domain.team.entity.Team;
import com.example.spartaoutsourcing.domain.team.repository.TeamRepository;
import com.example.spartaoutsourcing.domain.member.repository.MemberRepository;
import com.example.spartaoutsourcing.domain.user.entity.User;
import com.example.spartaoutsourcing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
        if(teamRepository.existsByName(teamRequest.getName())){
            throw new GlobalException(ErrorCode.TEAM_NAME_DUPLICATED);
        }

        Team team = Team.of(teamRequest.getName(), teamRequest.getDescription(), new ArrayList<>());
        teamRepository.save(team);

        return TeamResponse.of(
                team.getId(),
                team.getName(),
                team.getDescription(),
                team.getCreatedAt(),
                members
        );
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
