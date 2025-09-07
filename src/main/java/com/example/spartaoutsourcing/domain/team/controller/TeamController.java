package com.example.spartaoutsourcing.domain.team.controller;

import com.example.spartaoutsourcing.common.dto.GlobalApiResponse;
import com.example.spartaoutsourcing.domain.member.dto.response.MemberResponse;
import com.example.spartaoutsourcing.domain.team.dto.request.TeamRequest;
import com.example.spartaoutsourcing.domain.team.dto.response.TeamResponse;
import com.example.spartaoutsourcing.common.consts.SuccessCode;
import com.example.spartaoutsourcing.domain.team.service.TeamService;
import com.example.spartaoutsourcing.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {
    private final TeamService teamService;

    /**
     * 팀 생성
     *
     * URL: POST /api/teams
     *
     * @param teamRequest 팀 생성 요청 정보 (TeamRequest)
     * @return 생성된 팀 정보(GlobalApiResponse<TeamResponse>)
     *         성공 시 SuccessCode.TEAM_CREATED 반환
     */
    @PostMapping
    public GlobalApiResponse<TeamResponse> save(
            @Valid @RequestBody TeamRequest teamRequest
    ){
        TeamResponse save = teamService.save(teamRequest);
        return GlobalApiResponse.of(SuccessCode.TEAM_CREATED, save);
    }

    /**
     * 전체 팀 조회
     *
     * URL: GET /api/teams
     *
     * @return 전체 팀 리스트(GlobalApiResponse<List<TeamResponse>>)
     *         성공 시 SuccessCode.SUCCESS_GET_TEAM 반환
     */
    @GetMapping
    public GlobalApiResponse<List<TeamResponse>> getTeams()
    {
        List<TeamResponse> result = teamService.getTeams();
        return GlobalApiResponse.of(SuccessCode.SUCCESS_GET_TEAM, result);
    }

    /**
     * 팀 정보 수정
     *
     * URL: PUT /api/teams/{teamId}
     *
     * @param teamId 수정할 팀 ID (PathVariable)
     * @param teamRequest 수정할 팀 정보 (TeamRequest)
     * @return 수정된 팀 정보(GlobalApiResponse<TeamResponse>)
     *         성공 시 SuccessCode.TEAM_UPDATE 반환
     */
    @PutMapping("/{teamId}")
    public GlobalApiResponse<TeamResponse> updateTeam(
            @PathVariable Long teamId,
            @RequestBody TeamRequest teamRequest
    ){
        TeamResponse updatedTeam = teamService.updateTeam(teamId, teamRequest);
        return GlobalApiResponse.of(SuccessCode.TEAM_UPDATE, updatedTeam);
    }

    /**
     * 팀 삭제
     *
     * URL: DELETE /api/teams/{teamId}
     *
     * @param teamId 삭제할 팀 ID (PathVariable)
     * @return 삭제 완료 후 응답(GlobalApiResponse<Void>)
     *         성공 시 SuccessCode.TEAM_DELETED 반환
     */
    @DeleteMapping("/{teamId}")
    public GlobalApiResponse<Void> delete(
            @PathVariable Long teamId
    ){
        teamService.delete(teamId);
        return GlobalApiResponse.of(SuccessCode.TEAM_DELETED, null);
    }

    /**
     * 단건 팀 조회
     *
     * URL: GET /api/teams/{teamId}
     *
     * @param teamId 조회할 팀 ID (PathVariable)
     * @return 팀 정보(GlobalApiResponse<TeamResponse>)
     *         성공 시 SuccessCode.TEAM_FOUND 반환
     */
    @GetMapping("/{teamId}")
    public GlobalApiResponse<TeamResponse> getTeam(
            @PathVariable Long teamId
    ){
        TeamResponse teamResponse = teamService.getTeamById(teamId);
        return GlobalApiResponse.of(SuccessCode.TEAM_FOUND, teamResponse);
    }

    /**
     * 팀 멤버 조회
     *
     * URL: GET /api/teams/{teamId}/members
     *
     * @param teamId 팀 ID (PathVariable)
     * @return 해당 팀 멤버 리스트(GlobalApiResponse<List<MemberResponse>>)
     *         성공 시 SuccessCode.TEAM_MEMBERS_RETRIEVED 반환
     */
    @GetMapping("/api/teams/{teamId}/members")
    public GlobalApiResponse<List<MemberResponse>> getTeamMembers(
            @PathVariable Long teamId
    ){
        List<MemberResponse> members = teamService.getMembersByTeamId(teamId);
        return GlobalApiResponse.of(SuccessCode.TEAM_MEMBERS_RETRIEVED, members);
    }

    /**
     * 특정 팀에 추가 가능한 유저 조회
     *
     * URL: GET /api/users/available?teamId={teamId}
     *
     * @param teamId 팀 ID (RequestParam)
     * @return 추가 가능한 유저 리스트(GlobalApiResponse<List<User>>)
     *         성공 시 SuccessCode.AVAILABLE_USER 반환
     */
    @GetMapping("/api/users/available")
    public GlobalApiResponse<List<User>> getAvailableUsers(
            @RequestParam Long teamId
    ){
        List<User> availableUsers = teamService.getAvailableUsers(teamId);
        return GlobalApiResponse.of(SuccessCode.AVAILABLE_USER, availableUsers);
    }
}
