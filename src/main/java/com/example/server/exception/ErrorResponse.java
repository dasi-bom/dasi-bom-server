package com.example.server.exception;

import static java.time.LocalDateTime.*;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
	private final int status;
	private final String message;
	private final String code;
	private final LocalDateTime timestamp;

	public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
		return ResponseEntity
			.status(errorCode.getHttpStatus())
			.body(ErrorResponse.builder()
				.status(errorCode.getHttpStatus().value())
				.message(errorCode.getMessage())
				.code(errorCode.getCode())
				.timestamp(now())
				.build()
			);
	}

	public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode, String errorMessage) {
		return ResponseEntity
			.status(errorCode.getHttpStatus())
			.body(ErrorResponse.builder()
				.status(errorCode.getHttpStatus().value())
				.message(errorMessage)
				.code(errorCode.getCode())
				.timestamp(now())
				.build()
			);
	}
}
