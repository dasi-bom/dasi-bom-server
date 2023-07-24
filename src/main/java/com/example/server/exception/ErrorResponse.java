package com.example.server.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Getter
@Builder
public class ErrorResponse {
    private final String error;
    private final String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(ErrorResponse.builder()
                .error(errorCode.getHttpStatus().name())
                .message(errorCode.getMessage())
                .build()
            );
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode, String defaultMessage) {
        return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(ErrorResponse.builder()
                .error(errorCode.getHttpStatus().name())
                .message(defaultMessage)
                .build()
            );
    }
}
