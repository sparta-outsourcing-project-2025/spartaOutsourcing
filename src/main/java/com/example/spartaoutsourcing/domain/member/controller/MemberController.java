package com.example.spartaoutsourcing.domain.member.controller;

import com.example.spartaoutsourcing.common.consts.SuccessCode;
import com.example.spartaoutsourcing.common.dto.GlobalApiResponse;
import com.example.spartaoutsourcing.domain.member.dto.request.MemberRequest;
import com.example.spartaoutsourcing.domain.member.service.MemberService;
import com.example.spartaoutsourcing.domain.team.dto.response.TeamResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams/{teamId}")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    public GlobalApiResponse<TeamResponse> saveMember(
            @PathVariable Long teamId,
            @RequestBody Map<String, Long> request
    ){
        Long userId = request.get("userId");
        TeamResponse response = memberService.saveMember(teamId, userId);
        return GlobalApiResponse.of(SuccessCode.MEMBER_ADDED, response);
    }
}
