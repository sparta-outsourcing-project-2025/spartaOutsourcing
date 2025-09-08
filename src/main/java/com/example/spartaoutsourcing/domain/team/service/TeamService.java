package com.example.spartaoutsourcing.domain.team.service;

import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.member.dto.response.MemberResponse;
import com.example.spartaoutsourcing.domain.team.dto.request.TeamRequest;
import com.example.spartaoutsourcing.domain.team.dto.response.TeamResponse;
import com.example.spartaoutsourcing.domain.team.entity.Team;
import com.example.spartaoutsourcing.domain.team.repository.TeamRepository;
import com.example.spartaoutsourcing.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    /**
     * 새로운 팀 생성
     *
     * @param teamRequest 팀 생성 요청 정보(이름, 설명)
     * @return 생성된 팀 정보(TeamResponse)
     * @throws GlobalException 팀 이름 중복 시 ErrorCode.TEAM_NAME_DUPLICATED
     */
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

    /**
     * 전체 팀 조회
     *
     * @return 팀 리스트(TeamResponse)
     */
    @Transactional(readOnly = true)
    public List<TeamResponse> getTeams() {
        return teamRepository.findAll().stream()
                .map(team -> TeamResponse.of(
                        team.getId(),
                        team.getName(),
                        team.getDescription(),
                        team.getCreatedAt(),
                        team.getMembers().stream()
                                .filter(member -> member.getUser() != null && member.getUser().getDeletedAt() == null)
                                .map(MemberResponse::from)
                                .toList()
                ))
                .toList();
    }

    /**
     * 팀 정보 수정
     *
     * @param teamId 수정할 팀 ID
     * @param teamRequest 수정할 팀 정보(이름, 설명)
     * @return 수정된 팀 정보(TeamResponse)
     * @throws GlobalException 팀이 존재하지 않을 경우 ErrorCode.TEAM_NOT_FOUND
     */
    @Transactional
    public TeamResponse updateTeam(Long teamId, TeamRequest teamRequest) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new GlobalException(ErrorCode.TEAM_NOT_FOUND));


        team.updateInfo(teamRequest.getName(), teamRequest.getDescription());

        return TeamResponse.from(team);
    }

    /**
     * 팀 삭제
     *
     * @param teamId 삭제할 팀 ID
     * @throws GlobalException 팀이 존재하지 않을 경우 ErrorCode.TEAM_NOT_FOUND
     */
    @Transactional
    public void delete(Long teamId){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new GlobalException(ErrorCode.TEAM_NOT_FOUND));
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

    /**
     * 단일 팀 조회
     *
     * @param teamId 조회할 팀 ID
     * @return 팀 정보(TeamResponse)
     * @throws GlobalException 팀이 존재하지 않을 경우 ErrorCode.TEAM_NOT_FOUND
     */
    @Transactional(readOnly = true)
    public TeamResponse getTeamById(Long teamId){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new GlobalException(ErrorCode.TEAM_NOT_FOUND));

        List<MemberResponse> memberResponses = team.getMembers().stream()
                .map(MemberResponse::from)
                .filter(Objects::nonNull)
                .toList();

        return TeamResponse.from(team);
    }

    /**
     * 팀 멤버 조회
     *
     * @param teamId 팀 ID
     * @return 팀 멤버 리스트(MemberResponse)
     * @throws GlobalException 팀이 존재하지 않을 경우 ErrorCode.TEAM_NOT_FOUND
     */
    @Transactional(readOnly=true)
    public List<MemberResponse> getMembersByTeamId(Long teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new GlobalException(ErrorCode.TEAM_NOT_FOUND));

        return team.getMembers().stream()
                .map(MemberResponse::from)
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * 팀에 추가 가능한 유저 조회
     *
     * @param teamId 팀 ID
     * @return 팀에 추가 가능한 유저 리스트(User)
     * @throws GlobalException 팀이 존재하지 않을 경우 ErrorCode.TEAM_NOT_FOUND
     */
    @Transactional(readOnly=true)
    public List<User> getAvailableUsers(Long teamId){
        if (!teamRepository.existsById(teamId)){
            throw new GlobalException(ErrorCode.TEAM_NOT_FOUND);
        }
        return teamRepository.findAvailableUsers(teamId);
    }
}
