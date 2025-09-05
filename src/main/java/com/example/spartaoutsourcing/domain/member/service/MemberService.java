package com.example.spartaoutsourcing.domain.member.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.member.dto.response.MemberResponse;
import com.example.spartaoutsourcing.domain.member.entity.Member;
import com.example.spartaoutsourcing.domain.member.repository.MemberRepository;
import com.example.spartaoutsourcing.domain.team.dto.response.TeamResponse;
import com.example.spartaoutsourcing.domain.team.entity.Team;
import com.example.spartaoutsourcing.domain.team.repository.TeamRepository;
import com.example.spartaoutsourcing.domain.user.entity.User;
import com.example.spartaoutsourcing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService{

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamResponse saveMember(Long teamId, Long userId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new GlobalException(ErrorCode.TEAM_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

        if (memberRepository.existsByTeamAndUser(team, user)){
            throw new GlobalException(ErrorCode.USER_ALREADY_TEAM_MEMBER);
        }

        Member member = new Member(team, user);
        team.saveMember(member);
        memberRepository.save(member);

        return TeamResponse.of(
                team.getId(),
                team.getName(),
                team.getDescription(),
                team.getCreatedAt(),
                team.getMembers().stream()
                        .map(MemberResponse::from)
                        .toList()
        );
    }

    public List<User> getMembersByTeamId(Long teamId) {
        return memberRepository.findByTeamId(teamId)
            .stream()
            .map(Member::getUser)
            .collect(Collectors.toList());
    }
}