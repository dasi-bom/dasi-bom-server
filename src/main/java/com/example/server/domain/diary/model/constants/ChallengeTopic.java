package com.example.server.domain.diary.model.constants;

import static com.example.server.global.exception.ErrorCode.*;

import java.util.Arrays;

import com.example.server.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ChallengeTopic {

	TP_1("날도 좋은데, 기분 좋은 산책일기"),
	TP_2("웃긴 표정을 찰칵! 유쾌일기"),
	TP_3("무슨 꿈을 꾸나? 쿨쿨일기"),
	ERROR("챌린지 에러");

	private final String message;

	public static ChallengeTopic toEnum(String ct) {
		ChallengeTopic challengeTopic = Arrays.stream(ChallengeTopic.values())
			.filter(value -> value.name().equals(ct.toUpperCase()))
			.findFirst()
			.orElse(ERROR);
		if (challengeTopic.equals(ERROR)) {
			throw new BusinessException(CATEGORY_INVALID);
		}
		return challengeTopic;
	}
}
