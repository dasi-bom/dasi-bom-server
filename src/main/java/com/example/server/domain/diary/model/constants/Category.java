package com.example.server.domain.diary.model.constants;

import static com.example.server.global.exception.ErrorCode.*;

import java.util.Arrays;

import com.example.server.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Category {

	DAILY("일상"),
	CHALLENGE_1("날도 좋은데, 기분 좋은 산책일기"),
	CHALLENGE_2("웃긴 표정을 찰칵! 유쾌일기"),
	CHALLENGE_3("무슨 꿈을 꾸나? 쿨쿨일기"),
	ERROR("카테고리 에러");

	private final String message;

	public static Category toEnum(String cg) {
		Category category = Arrays.stream(Category.values())
			.filter(value -> value.name().equals(cg.toUpperCase()))
			.findFirst()
			.orElse(ERROR);
		if (category.equals(ERROR)) {
			throw new BusinessException(CATEGORY_INVALID);
		}
		return category;
	}
}
