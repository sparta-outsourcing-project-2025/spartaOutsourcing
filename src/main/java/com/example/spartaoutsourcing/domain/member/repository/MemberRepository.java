package com.example.spartaoutsourcing.domain.member.repository;

import com.example.spartaoutsourcing.domain.member.entity.Member;
import com.example.spartaoutsourcing.domain.team.entity.Team;
import com.example.spartaoutsourcing.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{
    boolean existsByTeamAndUser(Team team, User user);
    Optional<Member> findByTeamIdAndUserId(Long teamId, Long userId);
    List<Member> findByUser(User user);
    List<Member> findByTeamId(Long teamId);
}
