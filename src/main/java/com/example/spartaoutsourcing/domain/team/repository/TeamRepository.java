package com.example.spartaoutsourcing.domain.team.repository;

import com.example.spartaoutsourcing.domain.team.entity.Team;
import com.example.spartaoutsourcing.domain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository <Team, Long> {
    boolean existsByName(String name);

    @Query("SELECT t FROM Team t WHERE (t.name LIKE %:keyword%) OR (t.description LIKE %:keyword%)")
    List<Team> findTeamsByKeyword(@Param("keyword") String keyword);

    @EntityGraph(attributePaths = "members")
    List<Team> findAll();

    Optional<Team> findFirstByOrderByCreatedAtAsc();

    List<User> findAvailableUsers(Long teamId);
    @Query("SELECT u FROM User u " +
            "WHERE u.deletedAt IS NULL AND NOT EXISTS (" +
            "SELECT 1 FROM Member m WHERE m.user = u AND m.team.id = :teamId)")
    List<User> findAvailableUsers(@Param("teamId") Long teamId);
}
