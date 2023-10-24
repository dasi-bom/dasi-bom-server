package com.example.server.global.exception.errorcode;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {
	REFRESH_TOKEN_NOT_FOUND(1000, "존재하지 않는 리프레시 토큰입니다", UNAUTHORIZED),
	REFRESH_TOKEN_EXPIRED(1001, "만료된 리프레시 토큰입니다", UNAUTHORIZED),
	ACCESS_TOKEN_INVALID(1002, "유효하지 않은 엑세스 토큰입니다", UNAUTHORIZED);

	private final int code;
	private final String description;
	private final HttpStatus httpStatus;
}
