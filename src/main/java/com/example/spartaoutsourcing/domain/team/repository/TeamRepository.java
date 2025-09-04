package com.example.spartaoutsourcing.domain.team.repository;

import com.example.spartaoutsourcing.domain.team.entity.Team;
import com.example.spartaoutsourcing.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends JpaRepository <Team, Long> {
    boolean existsByName(String name);

    @Query("SELECT t FROM Team t WHERE (t.name LIKE %:keyword%) OR (t.description LIKE %:keyword%)")
    List<Team> findTeamsByKeyword(@Param("keyword") String keyword);
}
