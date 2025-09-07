package com.example.spartaoutsourcing.domain.member.controller;

import com.example.spartaoutsourcing.common.consts.SuccessCode;
import com.example.spartaoutsourcing.common.dto.GlobalApiResponse;
import com.example.spartaoutsourcing.common.exception.GlobalException;
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

    /**
     * 팀에 유저를 멤버로 추가
     *
     * URL: POST /api/teams/{teamId}/members
     *
     * @param teamId 팀 ID (PathVariable)
     * @param request 요청 Body에 담긴 userId 맵 {"userId": 1}
     * @return 추가 후 팀 정보와 현재 팀 멤버 리스트를 담은 GlobalApiResponse<TeamResponse>
     *         성공 시 SuccessCode.MEMBER_ADDED 반환
     * @throws GlobalException 팀이 존재하지 않거나 유저가 존재하지 않거나 이미 팀 멤버인 경우
     */
    @PostMapping("/members")
    public GlobalApiResponse<TeamResponse> saveMember(
            @PathVariable Long teamId,
            @RequestBody Map<String, Long> request
    ){
        Long userId = request.get("userId");
        TeamResponse response = memberService.saveMember(teamId, userId);
        return GlobalApiResponse.of(SuccessCode.MEMBER_ADDED, response);
    }

    /**
     * 팀에서 특정 유저를 멤버에서 제거
     *
     * URL: DELETE /api/teams/{teamId}/members/{userId}
     *
     * @param teamId 팀 ID (PathVariable)
     * @param userId 제거할 유저 ID (PathVariable)
     * @return 제거 후 팀 정보와 현재 팀 멤버 리스트를 담은 GlobalApiResponse<TeamResponse>
     *         성공 시 SuccessCode.MEMBER_REMOVE 반환
     */
    @DeleteMapping("/members/{userId}")
    public GlobalApiResponse<TeamResponse> deleteMember(
            @PathVariable Long teamId,
            @PathVariable Long userId
    ){
        TeamResponse response = memberService.removeMember(teamId, userId);
        return GlobalApiResponse.of(SuccessCode.MEMBER_REMOVE, response);
    }
}
