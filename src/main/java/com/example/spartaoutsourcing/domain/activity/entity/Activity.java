package com.example.spartaoutsourcing.domain.activity.entity;

import com.example.spartaoutsourcing.common.entity.BaseEntity;
import com.example.spartaoutsourcing.domain.activity.enums.ActivityType;
import com.example.spartaoutsourcing.domain.task.entity.Task;
import com.example.spartaoutsourcing.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name="activities")
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Activity extends BaseEntity {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActivityType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(nullable = false)
    private String description;

    private Activity(ActivityType type, User user, Task task, String description) {
        this.type = type;
        this.user = user;
        this.task = task;
        this.description = description;
    }

    public static Activity of(ActivityType type, User user, Task task, String description) {
        return new Activity(type, user, task, description);
    }
}
