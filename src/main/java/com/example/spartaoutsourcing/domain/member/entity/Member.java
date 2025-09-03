package com.example.spartaoutsourcing.domain.member.entity;

import com.example.spartaoutsourcing.common.entity.AuditableEntity;
import com.example.spartaoutsourcing.domain.team.entity.Team;
import com.example.spartaoutsourcing.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "members")
@EntityListeners(AuditingEntityListener.class)
public class Member extends AuditableEntity {

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="team_id")
    private Team team;

    public Member(Team team, User user){
        this.team = team;
        this.user = user;
    }

}
