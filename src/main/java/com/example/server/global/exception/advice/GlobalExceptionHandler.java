package com.example.server.global.exception.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.server.global.exception.BusinessException;
import com.example.server.global.exception.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<ErrorResponse> handleCustomException(BusinessException ex) {
		log.error("handleCustomException throw BusinessException : {}", ex.getErrorCode());
		return ErrorResponse.toResponseEntity(ex.getErrorCode());
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
		log.error("handleException", ex);
		return ErrorResponse.toResponseEntity(ex.getLocalizedMessage());
	}

}
