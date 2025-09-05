package com.example.spartaoutsourcing.domain.member.service;

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
import com.example.spartaoutsourcing.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService{

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public TeamResponse saveMember(Long teamId, Long userId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new GlobalException(ErrorCode.TEAM_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));


        if (user.getDeletedAt() != null) {
            throw new GlobalException(ErrorCode.USER_NOT_FOUND);
        }

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
                        .filter(m -> m.getUser() != null && m.getUser().getDeletedAt() == null)
                        .map(MemberResponse::from)
                        .toList()
        );
    }

    @Transactional
    public TeamResponse removeMember(Long teamId, Long userId){
        Team team = teamRepository.findById(teamId).orElse(null);

        Member member = memberRepository.findByTeamIdAndUserId(teamId, userId)
                .orElse(null);

        if (member!=null){
            team.getMembers().remove(member);
            memberRepository.delete(member);
        }

        return TeamResponse.of(
                team.getId(),
                team.getName(),
                team.getDescription(),
                team.getCreatedAt(),
                team.getMembers().stream()
                        .filter(m -> m.getUser() != null && m.getUser().getDeletedAt() == null)
                        .map(MemberResponse::from)
                        .toList()
        );
    }

    /**
     * 유저가 탈퇴할 때 해당 유저를 팀 멤버에서 제거
     */
    @Transactional
    public void removeMemberByUser(User user){
        List<Member> members = memberRepository.findByUser(user);
        memberRepository.deleteAll(members);

    }

}