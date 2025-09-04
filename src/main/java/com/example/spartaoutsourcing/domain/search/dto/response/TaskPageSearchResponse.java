package com.example.spartaoutsourcing.domain.search.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskPageSearchResponse {
    private final List<TaskContentSearchResponse> content;
    private final Long totalElements;
    private final Integer totalPages;
    private final Integer size;
    private final Integer number;

    public static TaskPageSearchResponse of(
            List<TaskContentSearchResponse> content,
            Long totalElements,
            Integer totalPages,
            Integer size,
            Integer number
    ){
        return new TaskPageSearchResponse(
                content,
                totalElements,
                totalPages,
                size,
                number
        );
    }
}
