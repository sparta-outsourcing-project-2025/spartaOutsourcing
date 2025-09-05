package com.example.spartaoutsourcing.domain.activity.repository;

import com.example.spartaoutsourcing.domain.activity.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity,Integer> {
}
