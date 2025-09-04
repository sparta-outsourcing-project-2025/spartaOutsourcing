package com.example.spartaoutsourcing.common.annotation.advice;

import com.example.spartaoutsourcing.common.consts.ErrorCode;
import com.example.spartaoutsourcing.common.dto.GlobalApiResponse;
import com.example.spartaoutsourcing.common.exception.GlobalException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<Object> globalHandler(GlobalException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(new GlobalApiResponse<>(false, errorCode.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalApiResponse<Object>> validation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .reduce((msg1, msg2) -> msg1 + ", " + msg2)
            .orElse("Validation failed");

        return ResponseEntity.badRequest()
            .body(new GlobalApiResponse<>(false, message, null));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GlobalApiResponse<Object>> handleInvalidEnum(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof InvalidFormatException invalidEx && invalidEx.getTargetType().isEnum()) {
            return ResponseEntity.badRequest()
                .body(new GlobalApiResponse<>(false, ErrorCode.INVALID_TASK_STATUS.getMessage(), null));
        }
        return ResponseEntity.badRequest()
            .body(new GlobalApiResponse<>(false, "잘못된 요청입니다.", null));
    }
}
