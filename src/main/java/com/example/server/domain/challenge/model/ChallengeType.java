package com.example.server.domain.challenge.model;

import static com.example.server.global.exception.ErrorCode.*;

import java.util.Arrays;

import com.example.server.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ChallengeType {

	PERIOD("기간 챌린지"),
	GOAL("목표 달성 챌린지"),
	PARTNERSHIP("외부 제휴 챌린지"),
	ERROR("챌린지 에러");

	private final String message;

	public static ChallengeType toEnum(String ch) {
		ChallengeType challenge = Arrays.stream(ChallengeType.values())
			.filter(value -> value.name().equals(ch.toUpperCase()))
			.findFirst()
			.orElse(ERROR);
		if (challenge.equals(ERROR)) {
			throw new BusinessException(CHALLENGE_INVALID);
		}
		return challenge;
	}
}
