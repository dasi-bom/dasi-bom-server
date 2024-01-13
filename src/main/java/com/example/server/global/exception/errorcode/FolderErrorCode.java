package com.example.server.global.exception.errorcode;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FolderErrorCode implements ErrorCode {
	FOLDER_NOT_FOUND(8000, "존재하지 않는 폴더입니다.", NOT_FOUND),
	FOLDER_CANNOT_ACCESS(8001, "폴더에 대한 접근 권한이 없습니다.", NOT_FOUND);

	private final int code;
	private final String description;
	private final HttpStatus httpStatus;
}
