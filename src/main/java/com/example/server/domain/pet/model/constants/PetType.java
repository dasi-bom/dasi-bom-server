package com.example.server.domain.pet.model.constants;

import static com.example.server.global.exception.ErrorCode.*;

import java.util.Arrays;

import com.example.server.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PetType {
	DOG("강아지"),
	CAT("고양이"),
	GOAT("염소"),
	FOX("여우"),
	ETC("기타"),
	ERROR("임시보호 타입 에러");

	private final String message;

	public static PetType toEnum(String petType) {
		PetType enumPetType = Arrays.stream(PetType.values())
			.filter(value -> value.name().equals(petType.toUpperCase()))
			.findFirst()
			.orElse(ERROR);
		if (enumPetType.equals(ERROR)) {
			throw new BusinessException(PET_TYPE_INVALID);
		}
		return enumPetType;
	}
}
