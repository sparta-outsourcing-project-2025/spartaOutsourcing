package com.example.spartaoutsourcing.domain.team.controller;

import com.example.spartaoutsourcing.common.dto.GlobalApiResponse;
import com.example.spartaoutsourcing.domain.team.dto.request.TeamRequest;
import com.example.spartaoutsourcing.domain.team.dto.response.TeamResponse;
import com.example.spartaoutsourcing.common.annotation.Auth;
import com.example.spartaoutsourcing.common.consts.SuccessCode;
import com.example.spartaoutsourcing.common.dto.AuthUserRequest;
import com.example.spartaoutsourcing.common.dto.GlobalApiResponse;
import com.example.spartaoutsourcing.domain.team.dto.request.TeamRequest;
import com.example.spartaoutsourcing.domain.team.dto.response.TeamResponse;
import com.example.spartaoutsourcing.domain.team.entity.Team;
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
        TeamResponse result = teamService.save(teamRequest);
        return GlobalApiResponse.of(SuccessCode.TEAM_CREATED, result);
    }

    @GetMapping
    public GlobalApiResponse<List<TeamResponse>> getTeams()
    {
        List<TeamResponse> result = teamService.getTeams();
        return GlobalApiResponse.of(SuccessCode.SUCCESS_GET_TEAM, result);
        TeamResponse save = teamService.save(teamRequest);
        return GlobalApiResponse.of(SuccessCode.TEAM_CREATED, save);
    }
}
