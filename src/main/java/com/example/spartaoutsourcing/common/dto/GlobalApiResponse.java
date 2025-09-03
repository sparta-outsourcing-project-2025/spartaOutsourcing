package com.example.spartaoutsourcing.common.dto;


import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.consts.SuccessCode;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class GlobalApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;


    public GlobalApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
    public static <T> GlobalApiResponse<T> of(SuccessCode code, T data) {
        return new GlobalApiResponse<>(
                true,
                code.getMessage(),
                data
        );
    }
    public static GlobalApiResponse<Object> of(ErrorCode code) {
        return new GlobalApiResponse<>(
                false,
                code.getMessage(),
                null
        );
    }
}
