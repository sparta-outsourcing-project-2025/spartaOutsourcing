package com.example.spartaoutsourcing.domain.team.repository;

import com.example.spartaoutsourcing.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository <Team, Long> {

    boolean existsByName(String name);
}
