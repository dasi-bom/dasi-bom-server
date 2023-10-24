package com.example.server.global.exception.errorcode;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChallengeErrorCode implements ErrorCode {
	CHALLENGE_INVALID(6000, "챌린지가 유효하지 않습니다", NOT_FOUND),
	CONFLICT_CHALLENGE(6001, "이미 존재하는 챌린지입니다", CONFLICT);

	private final int code;
	private final String description;
	private final HttpStatus httpStatus;
}
