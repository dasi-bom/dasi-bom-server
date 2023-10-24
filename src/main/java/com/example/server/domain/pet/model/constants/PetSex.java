package com.example.server.domain.pet.model.constants;

import java.util.Arrays;

import com.example.server.global.exception.BusinessException;
import com.example.server.global.exception.errorcode.PetErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PetSex {
	MALE("수컷"),
	FEMALE("암컷"),
	ERROR("동물 성별 에러");

	private final String message;

	public static PetSex toEnum(String sex) {
		PetSex petSex = Arrays.stream(PetSex.values())
			.filter(value -> value.name().equals(sex.toUpperCase()))
			.findFirst()
			.orElse(ERROR);
		if (petSex.equals(ERROR)) {
			throw new BusinessException(PetErrorCode.PET_SEX_INVALID);
		}
		return petSex;
	}
}
