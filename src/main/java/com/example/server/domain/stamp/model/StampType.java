package com.example.server.domain.stamp.model;

import static com.example.server.global.exception.ErrorCode.*;

import java.util.Arrays;

import com.example.server.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StampType {
	WALK("산책"),
	TREAT("간식"),
	TOY("장난감"),
	ERROR("스탬프 에러");

	private final String message;

	public static StampType toEnum(String type) {
		StampType stampType = Arrays.stream(StampType.values())
			.filter(value -> value.name().equals(type.toUpperCase()))
			.findFirst()
			.orElse(ERROR);
		if (stampType.equals(ERROR)) {
			throw new BusinessException(STAMP_INVALID);
		}
		return stampType;
	}
}
