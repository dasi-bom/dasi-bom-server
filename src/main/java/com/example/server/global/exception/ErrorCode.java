package com.example.server.global.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	/**
	 * GLOBAL ERROR CODE (GL)
	 */
	CONSTRAINT_VIOLATION("GL-01-01", BAD_REQUEST, "제약 조건을 위배한 요청입니다 (constraint violation)"),
	METHOD_ARG_NOT_VALID("GL-01-02", BAD_REQUEST, "제약 조건을 위배한 요청입니다 (method argument not valid)"),
	FILE_NOT_EXIST_ERROR("GL-01-03", BAD_REQUEST, "파일이 존재하지 않습니다."),
	GLOBAL_INTERNAL_SERVER_ERROR("GL-01-04", INTERNAL_SERVER_ERROR, "Server Error!"),

	/**
	 * MEMBER ERROR CODE (ME)
	 */
	MEMBER_NOT_FOUND("ME-01-01", NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
	CONFLICT_NICKNAME("ME-01-02", CONFLICT, "이미 사용중인 nickname 입니다"),
	SOCIAL_LOGIN_ERROR("ME-01-03", INTERNAL_SERVER_ERROR, "소셜 로그인에 실패했습니다."),

	/**
	 * PET ERROR CODE (PE)
	 */

	// PE-01 -> Pet Owner Error
	PET_OWNER_NULL("PE-01-01", BAD_REQUEST, "반려동물의 주인이 요청되지 않았습니다."),
	PET_OWNER_INVALID("PE-01-02", FORBIDDEN, "해당 사용자는 요청한 반려동물 정보에 접근할 수 없습니다."),

	// PE-02 -> Pet NAME Error
	PET_NAME_NULL("PE-02-01", BAD_REQUEST, "반려동물의 이름이 요청되지 않았습니다."),
	PET_NAME_TOO_LONG("PE-02-02", PAYLOAD_TOO_LARGE, "반려동물의 이름은 20자 이하여야 합니다."),
	PET_NAME_INVALID_CHARACTERS("PE-02-03", BAD_REQUEST, "반려동물의 이름에는 한글과 영어만 사용 가능합니다."),

	// PE-03 -> Pet Type Error
	PET_TYPE_NULL("PE-03-01", BAD_REQUEST, "반려동물의 종류가 요청되지 않았습니다."),
	PET_TYPE_INVALID("PE-03-02", BAD_REQUEST, "반려동물의 종류가 유효하지 않습니다."),

	// PE-04 -> Pet Age Error
	PET_AGE_INVALID("PE-04-01", BAD_REQUEST, "반려동물의 나이는 1~100 사이의 정수여야 합니다."),

	// PE-05 -> Pet Profile Image Error
	PET_PROFILE_IMAGE_INVALID("PE-05-01", BAD_REQUEST, "반려동물의 프로필 사진이 요청되지 않았습니다."),

	// PE-06 -> Pet Bio Error
	PET_BIO_TOO_LONG("PE-06-01", BAD_REQUEST, "반려동물의 소개는 공백 포함 300자 이내입니다."),

	// PE-07 -> Pet Temp Protected Date Error
	PET_TEMP_PROTECTED_SRT_DATE_TOO_EARLY("PE-07-01", BAD_REQUEST, "반려동물 임시보호 시작일이 너무 이전입니다."),
	PET_TEMP_PROTECTED_SRT_DATE_IN_FUTURE("PE-07-02", BAD_REQUEST, "반려동물 임시보호 시작일은 미래일 수 없습니다."),
	PET_TEMP_PROTECTED_SRT_DATE_INVALID("PE-07-03", BAD_REQUEST, "반려동물 임시보호 시작일이 유효하지 않습니다."),

	// PE-08 -> Pet Entity Error
	PET_NOT_FOUND("PE-08-01", NOT_FOUND, "반려 동물 정보를 찾을 수 없습니다."),

	// PE-09 -> Pet Sex Error
	PET_SEX_INVALID("PE-08-01", NOT_FOUND, "반려 동물 성별이 유효하지 않습니다."),

	/**
	 * STAMP ERROR CODE (ME)
	 */
	STAMP_INVALID("ST-01-01", NOT_FOUND, "스탬프가 유효하지 않습니다"),
	CONFLICT_STAMP("ST-02-01", CONFLICT, "이미 존재하는 스탬프입니다"),

	/**
	 * CATEGORY ERROR CODE (CA)
	 */
	CATEGORY_INVALID("CA-01-01", NOT_FOUND, "카테고리가 유효하지 않습니다"),

	/**
	 * CHALLENGE ERROR CODE (CH)
	 */
	CHALLENGE_TOPIC_INVALID("CH-01-01", NOT_FOUND, "챌린지가 유효하지 않습니다"),
	;

	private final String code;
	private final HttpStatus httpStatus;
	private final String message;

}
