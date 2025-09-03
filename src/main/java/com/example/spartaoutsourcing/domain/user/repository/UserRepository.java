package com.example.spartaoutsourcing.domain.user.repository;

import com.example.spartaoutsourcing.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
