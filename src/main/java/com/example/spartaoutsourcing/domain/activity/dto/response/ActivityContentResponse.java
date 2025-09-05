package com.example.spartaoutsourcing.domain.activity.dto.response;

import com.example.spartaoutsourcing.domain.activity.enums.ActivityType;
import com.example.spartaoutsourcing.domain.user.dto.response.UserResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivityContentResponse {
    private final Long id;
    private final ActivityType type;
    private final Long userId;
    private final UserResponse user;
    private final Long taskId;
    private final LocalDateTime timestamp;
    private final String description;

    public static ActivityContentResponse of(
            Long id,
            ActivityType type,
            Long userId,
            UserResponse userResponse,
            Long taskId,
            LocalDateTime timestamp,
            String description) {
        return new ActivityContentResponse(
                id,
                type,
                userId,
                userResponse,
                taskId,
                timestamp,
                description
        );
    }
}
