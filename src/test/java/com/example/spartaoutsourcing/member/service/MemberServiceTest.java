package com.example.spartaoutsourcing.member.service;

import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.example.spartaoutsourcing.domain.member.dto.response.MemberResponse;
import com.example.spartaoutsourcing.domain.member.entity.Member;
import com.example.spartaoutsourcing.domain.member.repository.MemberRepository;
import com.example.spartaoutsourcing.domain.member.service.MemberService;
import com.example.spartaoutsourcing.domain.team.entity.Team;
import com.example.spartaoutsourcing.domain.team.repository.TeamRepository;
import com.example.spartaoutsourcing.domain.team.dto.response.TeamResponse;
import com.example.spartaoutsourcing.domain.user.entity.User;
import com.example.spartaoutsourcing.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MemberService memberService;

    private Team team;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        team = Team.of("개발팀", "백엔드/프론트");
        ReflectionTestUtils.setField(team, "id", 1L);

        user = User.of("chulsoo", "chulsoo@example.com", "password", "철수", null);
        ReflectionTestUtils.setField(user, "id", 1L);
    }

    @Test
    void saveMember_성공() {
        // given
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(memberRepository.existsByTeamAndUser(team, user)).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        TeamResponse response = memberService.saveMember(1L, 1L);

        // then
        assertEquals(team.getName(), response.getName());
        assertEquals(team.getDescription(), response.getDescription());
        assertEquals(1, response.getMembers().size());
        MemberResponse memberResponse = response.getMembers().get(0);
        assertEquals(user.getUsername(), memberResponse.getUsername()); // DTO 필드 기준 검증
    }

    @Test
    void saveMember_팀존재하지않음_멤버추가시_예외발생() {
        //given
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        GlobalException exception = assertThrows(GlobalException.class, () -> memberService.saveMember(1L, 1L));
        assertEquals(ErrorCode.TEAM_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void saveMember_팀에추가하려는유저없음_예외발생() {
        // given
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        GlobalException exception = assertThrows(GlobalException.class, () -> memberService.saveMember(1L, 1L));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void removeMember_팀에존재하는유저_삭제성공() {
        // given
        Member member = new Member(team, user);
        team.saveMember(member);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(memberRepository.findByTeamIdAndUserId(1L, 1L)).thenReturn(Optional.of(member));

        // when
        TeamResponse response = memberService.removeMember(1L, 1L);

        // then
        assertTrue(response.getMembers().isEmpty());
    }

    @Test
    void removeMember_팀존재하지않음_멤버삭제시_예외발생() {
        // given
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        GlobalException exception = assertThrows(GlobalException.class, () -> memberService.removeMember(1L, 1L));
        assertEquals(ErrorCode.TEAM_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void removeMember_팀에존재하지않는유저_삭제시_예외발생(){
        // given
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(memberRepository.findByTeamIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

        // when & then
        GlobalException exception = assertThrows(GlobalException.class, () -> memberService.removeMember(1L, 1L));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void getMembersByTeamId_팀에_존재하는_사용자목록_조회_성공()  {
        // given
        Member member = new Member(team, user);
        when(memberRepository.findByTeamId(1L)).thenReturn(List.of(member));

        // when
        List<User> members = memberService.getMembersByTeamId(1L);

        // then
        assertEquals(1, members.size());
        assertEquals(user, members.get(0));
    }

    @Test
    void removeMemberByUser_팀에_존재하는_사용자_삭제_성공() {
        // given
        Member member = new Member(team, user);
        when(memberRepository.findByUser(user)).thenReturn(List.of(member));

        //when
        memberService.removeMemberByUser(user);

        //then
        verify(memberRepository, times(1)).deleteAll(List.of(member));
    }
}
