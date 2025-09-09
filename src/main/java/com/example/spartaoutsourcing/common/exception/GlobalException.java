package com.example.spartaoutsourcing.common.exception;


import com.example.spartaoutsourcing.common.consts.ErrorCode;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException{
    private ErrorCode errorCode;

    public GlobalException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

