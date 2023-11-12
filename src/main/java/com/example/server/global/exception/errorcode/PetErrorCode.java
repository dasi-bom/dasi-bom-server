package com.example.server.global.exception.errorcode;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PetErrorCode implements ErrorCode {
	PET_OWNER_NULL(3000, "반려동물의 주인이 요청되지 않았습니다.", BAD_REQUEST),
	PET_OWNER_INVALID(3001, "해당 사용자는 요청한 반려동물 정보에 접근할 수 없습니다.", FORBIDDEN),
	PET_NAME_NULL(3002, "반려동물의 이름이 요청되지 않았습니다.", BAD_REQUEST),
	PET_NAME_TOO_LONG(3003, "반려동물의 이름은 20자 이하여야 합니다.", PAYLOAD_TOO_LARGE),
	PET_NAME_INVALID_CHARACTERS(3004, "반려동물의 이름에는 한글과 영어만 사용 가능합니다.", BAD_REQUEST),
	PET_TYPE_NULL(3005, "반려동물의 종류가 요청되지 않았습니다.", BAD_REQUEST),
	PET_TYPE_INVALID(3006, "반려동물의 종류가 유효하지 않습니다.", BAD_REQUEST),
	PET_AGE_INVALID(3007, "반려동물의 나이는 1~100 사이의 정수여야 합니다.", BAD_REQUEST),
	PET_PROFILE_IMAGE_INVALID(3008, "반려동물의 프로필 사진이 요청되지 않았습니다.", BAD_REQUEST),
	PET_BIO_TOO_LONG(3009, "반려동물의 소개는 공백 포함 300자 이내입니다.", BAD_REQUEST),
	PET_TEMP_PROTECTED_SRT_DATE_TOO_EARLY(3010, "반려동물 임시보호 시작일이 너무 이전입니다.", BAD_REQUEST),
	PET_TEMP_PROTECTED_SRT_DATE_IN_FUTURE(3011, "반려동물 임시보호 시작일은 미래일 수 없습니다.", BAD_REQUEST),
	PET_TEMP_PROTECTED_SRT_DATE_INVALID(3012, "반려동물 임시보호 시작일이 유효하지 않습니다.", BAD_REQUEST),
	PET_NOT_FOUND(3013, "반려 동물 정보를 찾을 수 없습니다.", NOT_FOUND),
	PET_SEX_INVALID(3014, "반려 동물 성별이 유효하지 않습니다.", NOT_FOUND),
	PET_ALREADY_EXIST(3015, "이미 존재하는 동물입니다.", CONFLICT);

	private final int code;
	private final String description;
	private final HttpStatus httpStatus;
}
