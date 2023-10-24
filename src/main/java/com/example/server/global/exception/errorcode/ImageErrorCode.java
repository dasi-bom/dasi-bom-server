package com.example.server.global.exception.errorcode;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageErrorCode implements ErrorCode {
	FILE_NOT_EXIST_ERROR(7000, "파일이 존재하지 않습니다.", BAD_REQUEST),
	INVALID_IMAGE_EXTENSION(7001, "확장자가 jpg, jpeg, png 인 파일만 업로드 가능합니다", NOT_FOUND),
	;

	private final int code;
	private final String description;
	private final HttpStatus httpStatus;
}
