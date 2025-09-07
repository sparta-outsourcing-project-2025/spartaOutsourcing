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
import jakarta.transaction.Transactional;
import com.example.spartaoutsourcing.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    /**
     * 팀에 유저를 멤버로 추가
     * @param teamId 팀 ID
     * @param userId 유저 ID
     * @return 추가 후 팀 정보와 현재 팀 멤버 리스트를 담은 TeamResponse
     * @Throws GlobalException 팀이 존재하지 않거나 유저가 존재하지 않거나 이미 팀 멤버인 경우
     */
    public TeamResponse saveMember(Long teamId, Long userId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new GlobalException(ErrorCode.TEAM_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));


        if (user.getDeletedAt() != null) {
            throw new GlobalException(ErrorCode.USER_NOT_FOUND);
        }

        if (memberRepository.existsByTeamAndUser(team, user)) {
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

    /**
     * 팀에서 특정 유저를 멤버에서 제거
     *
     * @param teamId 팀 ID
     * @param userId 제거할 유저 ID
     * @return 제거 후 팀 정보와 현재 팀 멤버 리스트를 담은 TeamResponse
     */
    @Transactional
    public TeamResponse removeMember(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId).orElse(null);

        Member member = memberRepository.findByTeamIdAndUserId(teamId, userId)
                .orElse(null);

        if (member != null) {
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

    public List<User> getMembersByTeamId(Long teamId) {
        return memberRepository.findByTeamId(teamId)
                .stream()
                .map(Member::getUser)
                .collect(Collectors.toList());
    }

    /**
     * 팀 ID로 팀 멤버(User) 리스트 조회
     *
     * @param teamId 팀 ID
     * @return 해당 팀에 속한 유저 리스트
     */
    public List<User> getMembersByTeamId(Long teamId) {
        return memberRepository.findByTeamId(teamId)
                .stream()
                .map(Member::getUser)
                .collect(Collectors.toList());
    }

    /**
     * 유저가 탈퇴할 때 해당 유저를 모든 팀에서 제거
     *
     * @param user 탈퇴하는 유저 엔티티
     */
    @Transactional
    public void removeMemberByUser(User user) {
        List<Member> members = memberRepository.findByUser(user);
        memberRepository.deleteAll(members);

    }
}