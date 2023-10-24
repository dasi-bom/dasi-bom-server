package com.example.server.global.exception.errorcode;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StampErrorCode implements ErrorCode {
	STAMP_INVALID(4000, "스탬프가 유효하지 않습니다", NOT_FOUND),
	CONFLICT_STAMP(4001, "이미 존재하는 스탬프입니다", CONFLICT),
	STAMP_LIST_SIZE_TOO_SHORT(4002, "스탬프는 최소 2개 이상 선택해야 합니다", BAD_REQUEST),
	STAMP_LIST_SIZE_TOO_LONG(4003, "스탬프는 최대 ?개까지 선택할 수 있습니다", BAD_REQUEST);

	private final int code;
	private final String description;
	private final HttpStatus httpStatus;
}
