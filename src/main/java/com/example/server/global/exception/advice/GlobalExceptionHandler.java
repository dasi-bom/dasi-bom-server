package com.example.server.global.exception.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.server.global.exception.BusinessException;
import com.example.server.global.exception.ErrorCode;
import com.example.server.global.exception.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<ErrorResponse> handleCustomException(BusinessException e) {
		log.error("handleCustomException throw BusinessException : {}", e.getErrorCode());
		return ErrorResponse.toResponseEntity(e.getErrorCode());
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleGlobalException(Exception e) {
		log.error("handleException", e);
		return ErrorResponse.toResponseEntity(ErrorCode.GLOBAL_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
	}

}
