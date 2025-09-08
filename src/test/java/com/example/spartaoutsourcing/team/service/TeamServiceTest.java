package com.example.spartaoutsourcing.team.service;

import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.member.dto.response.MemberResponse;
import com.example.spartaoutsourcing.domain.member.entity.Member;
import com.example.spartaoutsourcing.domain.team.dto.request.TeamRequest;
import com.example.spartaoutsourcing.domain.team.dto.response.TeamResponse;
import com.example.spartaoutsourcing.domain.team.entity.Team;
import com.example.spartaoutsourcing.domain.team.repository.TeamRepository;
import com.example.spartaoutsourcing.domain.team.service.TeamService;
import com.example.spartaoutsourcing.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;

    private Team team;

    @BeforeEach
    void setUp() {
        teamRepository = mock(TeamRepository.class);
        teamService = new TeamService(teamRepository);

        team = Team.of("개발팀", "백엔드/프론트");
        ReflectionTestUtils.setField(team, "id", 1L);
    }

    @Test
    void 팀을_성공적으로_생성() {
        // given
        TeamRequest request = new TeamRequest();
        ReflectionTestUtils.setField(request, "name", "개발팀");
        ReflectionTestUtils.setField(request, "description", "백엔드/프론트");

        when(teamRepository.existsByName("개발팀")).thenReturn(false);
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        // when
        TeamResponse response = teamService.save(request);

        // then
        assertEquals("개발팀", response.getName());
        assertEquals("백엔드/프론트", response.getDescription());
    }

    @Test
    void 팀이_이미_존재할_때_예외발생() {
        // given
        TeamRequest request = new TeamRequest();
        ReflectionTestUtils.setField(request, "name", "개발팀");
        ReflectionTestUtils.setField(request, "description", "백엔드/프론트");

        when(teamRepository.existsByName("개발팀")).thenReturn(true);

        // when & then
        GlobalException exception = assertThrows(GlobalException.class, () -> teamService.save(request));
        assertEquals(ErrorCode.TEAM_NAME_DUPLICATED, exception.getErrorCode());
    }

    @Test
    void 존재하지_않는_팀을_조회하면_예외처리() {
        // given
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        GlobalException exception = assertThrows(GlobalException.class, () -> teamService.getTeamById(1L));
        assertEquals(ErrorCode.TEAM_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 팀을_성공적으로_조회() {
        // given
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        // when
        TeamResponse response = teamService.getTeamById(1L);

        // then
        assertEquals("개발팀", response.getName());
        assertEquals("백엔드/프론트", response.getDescription());
    }

    @Test
    void 팀을_성공적으로_삭제() {
        // given
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        // when
        teamService.delete(1L);

        // then
        verify(teamRepository).delete(team);
    }

    @Test
    void 존재하지_않는_팀을_삭제하면_예외발생() {
        // given
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        GlobalException exception = assertThrows(GlobalException.class, () -> teamService.delete(1L));
        assertEquals(ErrorCode.TEAM_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 팀을_성공적으로_수정() {
        // given
        TeamRequest request = new TeamRequest();
        ReflectionTestUtils.setField(request, "name", "디자인팀");
        ReflectionTestUtils.setField(request, "description", "UI/UX 담당");

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        // when
        TeamResponse response = teamService.updateTeam(1L, request);

        // then
        assertEquals("디자인팀", response.getName());
        assertEquals("UI/UX 담당", response.getDescription());
    }

    @Test
    void 존재하지_않는_팀을_수정하면_예외발생() {
        // given
        TeamRequest request = new TeamRequest();
        ReflectionTestUtils.setField(request, "name", "디자인팀");
        ReflectionTestUtils.setField(request, "description", "UI/UX 담당");

        when(teamRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        GlobalException exception = assertThrows(GlobalException.class, () -> teamService.updateTeam(999L, request));
        assertEquals(ErrorCode.TEAM_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 팀의_멤버를_성공적으로_조회() {
        // given
        User user = mock(User.class);
        Member member = mock(Member.class);
        when(member.getUser()).thenReturn(user);

        team.getMembers().add(member);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        // when
        List<MemberResponse> members = teamService.getMembersByTeamId(1L);

        // then
        assertEquals(1, members.size());
    }
}
