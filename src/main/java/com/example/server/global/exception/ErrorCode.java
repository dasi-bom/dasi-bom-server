package com.example.server.global.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	CONSTRAINT_VIOLATION("GL001", BAD_REQUEST, "제약 조건을 위배한 요청입니다 (constraint violation)"),
	METHOD_ARG_NOT_VALID("GL002", BAD_REQUEST, "제약 조건을 위배한 요청입니다 (method argument not valid)"),
	FILE_NOT_EXIST_ERROR("GL003", BAD_REQUEST, "파일이 존재하지 않습니다."),
	MEMBER_NOT_FOUND("ME001", NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
	CONFLICT_NICKNAME("ME002", CONFLICT, "이미 사용중인 nickname 입니다"),
	SOCIAL_LOGIN_ERROR("OA001", INTERNAL_SERVER_ERROR, "소셜 로그인에 실패했습니다."),
	GLOBAL_INTERNAL_SERVER_ERROR("OA003", INTERNAL_SERVER_ERROR, "Server Error!"),

	/**
	 * PET ERROR CODE (PE)
	 */

	// PE-01 -> Pet Owner Error
	PET_OWNER_NULL("PE-01-01", BAD_REQUEST, "반려동물의 주인이 요청되지 않았습니다."),

	// PE-02 -> Pet NAME Error
	PET_NAME_NULL("PE-02-01", BAD_REQUEST, "반려동물의 이름이 요청되지 않았습니다."),
	PET_NAME_TOO_LONG("PE-02-01", BAD_REQUEST, "반려동물의 이름은 20자 이하여야 합니다."),
	PET_NAME_INVALID_CHARACTERS("PE-02-02", BAD_REQUEST, "반려동물의 이름에는 한글과 영어만 사용 가능합니다."),

	// PE-03 -> Pet Type Error
	PET_TYPE_NULL("PE-03-03", BAD_REQUEST, "반려동물의 종류가 요청되지 않았습니다."),

	// PE-04 -> Pet Age Error
	PET_AGE_INVALID("PE-04-01", BAD_REQUEST, "반려동물의 나이는 1~100 사이의 정수여야 합니다."),

	// PE-05 -> Pet Profile Image Error
	// PE-06 -> Pet Bio Error
	PET_BIO_TOO_LONG("PE-06-01", BAD_REQUEST, "반려동물의 이름은 20자 이하여야 합니다."),

	// PE-07 -> Protected Date Error
	PET_PROTECTED_DATE_INVALID("PE-07-01", BAD_REQUEST, "반려동물 임시보호 시작일이 유효하지 않습니다.");

	private final String code;
	private final HttpStatus httpStatus;
	private final String message;

}
