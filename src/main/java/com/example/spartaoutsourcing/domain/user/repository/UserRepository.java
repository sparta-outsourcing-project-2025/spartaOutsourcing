package com.example.spartaoutsourcing.domain.user.repository;

import com.example.spartaoutsourcing.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsUserByUsername(String username);

    boolean existsUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE (u.name LIKE %:keyword%) OR (u.email LIKE %:keyword%)")
    List<User> findUsersByKeyword(@Param("keyword") String keyword);
}
