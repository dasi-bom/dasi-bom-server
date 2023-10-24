package com.example.server.global.exception.errorcode;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiaryErrorCode implements ErrorCode {
	MAX_IMAGE_ATTACHMENTS_EXCEEDED(5000, "최대 이미지 첨부 수는 5개입니다.", NOT_FOUND),
	DIARY_CONTENT_TOO_LONG(5001, "일기 본문은 공백 포함 1000자 이내입니다.", BAD_REQUEST),
	DIARY_NOT_FOUND(5002, "해당 일기 정보를 찾을 수 없습니다", NOT_FOUND);

	private final int code;
	private final String description;
	private final HttpStatus httpStatus;
}
