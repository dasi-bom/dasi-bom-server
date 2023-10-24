package com.example.server.global.exception;

import static java.time.LocalDateTime.*;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.server.global.exception.errorcode.ErrorCode;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
	private final int status;
	private final String description;
	private final int code;
	private final LocalDateTime timestamp;

	public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
		return ResponseEntity
			.status(errorCode.getHttpStatus())
			.body(ErrorResponse.builder()
				.status(errorCode.getHttpStatus().value())
				.description(errorCode.getDescription())
				.code(errorCode.getCode())
				.timestamp(now())
				.build()
			);
	}

	public static ResponseEntity<ErrorResponse> toResponseEntity(String errorMessage) {
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ErrorResponse.builder()
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.description(errorMessage)
				.code(0000)
				.timestamp(now())
				.build()
			);
	}
}
