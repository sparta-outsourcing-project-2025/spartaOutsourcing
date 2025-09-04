package com.example.spartaoutsourcing.domain.activities.dto.response;

import com.example.spartaoutsourcing.domain.activities.enums.ActivitiesType;
import com.example.spartaoutsourcing.domain.user.dto.response.UserResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivitiesContentResponse {
    private final Long id;
    private final ActivitiesType type;
    private final Long userId;
    private final UserResponse user;
    private final Long taskId;
    private final LocalDateTime timestamp;
    private final String description;

    public static ActivitiesContentResponse of(
            Long id,
            ActivitiesType type,
            Long userId,
            UserResponse userResponse,
            Long taskId,
            LocalDateTime timestamp,
            String description) {
        return new ActivitiesContentResponse(
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
