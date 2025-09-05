package com.example.spartaoutsourcing.domain.team.controller;

import com.example.spartaoutsourcing.common.dto.GlobalApiResponse;
import com.example.spartaoutsourcing.domain.member.dto.response.MemberResponse;
import com.example.spartaoutsourcing.domain.team.dto.request.TeamRequest;
import com.example.spartaoutsourcing.domain.team.dto.response.TeamResponse;
import com.example.spartaoutsourcing.common.consts.SuccessCode;
import com.example.spartaoutsourcing.domain.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    public GlobalApiResponse<TeamResponse> save(
            @Valid @RequestBody TeamRequest teamRequest
    ){
        TeamResponse save = teamService.save(teamRequest);
        return GlobalApiResponse.of(SuccessCode.TEAM_CREATED, save);
    }

    @GetMapping
    public GlobalApiResponse<List<TeamResponse>> getTeams()
    {
        List<TeamResponse> result = teamService.getTeams();
        return GlobalApiResponse.of(SuccessCode.SUCCESS_GET_TEAM, result);
    }

    @PutMapping("/{teamId}")
    public GlobalApiResponse<TeamResponse> updateTeam(
            @PathVariable Long teamId,
            @RequestBody TeamRequest teamRequest
    ){
        TeamResponse updatedTeam = teamService.updateTeam(teamId, teamRequest);
        return GlobalApiResponse.of(SuccessCode.TEAM_UPDATE, updatedTeam);
    }

    @DeleteMapping("/{teamId}")
    public GlobalApiResponse<Void> delete(
            @PathVariable Long teamId
    ){
        teamService.delete(teamId);
        return GlobalApiResponse.of(SuccessCode.TEAM_DELETED, null);
    }

    @GetMapping("/{teamId}")
    public GlobalApiResponse<TeamResponse> getTeam(
            @PathVariable Long teamId
    ){
        TeamResponse teamResponse = teamService.getTeamById(teamId);
        return GlobalApiResponse.of(SuccessCode.TEAM_FOUND, teamResponse);
    }

    @GetMapping("/{teamId}/members")
    public GlobalApiResponse<List<MemberResponse>> getTeamMembers(
            @PathVariable Long teamId
    ){
        List<MemberResponse> members = teamService.getMembersByTeamId(teamId);
        return GlobalApiResponse.of(SuccessCode.TEAM_MEMBERS_RETRIEVED, members);
    }
}
