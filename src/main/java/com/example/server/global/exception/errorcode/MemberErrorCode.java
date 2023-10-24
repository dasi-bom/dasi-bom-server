package com.example.server.global.exception.errorcode;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements ErrorCode {
	MEMBER_NOT_FOUND(2000, "해당 유저 정보를 찾을 수 없습니다", NOT_FOUND),
	CONFLICT_NICKNAME(2001, "이미 사용중인 nickname 입니다", CONFLICT),
	SOCIAL_LOGIN_ERROR(2002, "소셜 로그인에 실패했습니다.", INTERNAL_SERVER_ERROR);

	private final int code;
	private final String description;
	private final HttpStatus httpStatus;
}
