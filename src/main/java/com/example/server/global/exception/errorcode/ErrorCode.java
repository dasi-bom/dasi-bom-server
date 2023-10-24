package com.example.server.global.exception.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
	int getCode();

	String getDescription();

	HttpStatus getHttpStatus();
}
