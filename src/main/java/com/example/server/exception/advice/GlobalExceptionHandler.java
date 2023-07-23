package com.example.server.exception.advice;

import static com.example.server.exception.ErrorCode.*;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.server.exception.CustomException;
import com.example.server.exception.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected Object handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
		HttpServletRequest request) {
		String defaultMessage = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
		return ErrorResponse.toResponseEntity(METHOD_ARG_NOT_VALID, defaultMessage);
	}

	@ExceptionHandler(value = {ConstraintViolationException.class, DataIntegrityViolationException.class})
	protected ResponseEntity<ErrorResponse> handleDataException() {
		log.error("handleDataException throw Exception : {}", CONSTRAINT_VIOLATION);
		return ErrorResponse.toResponseEntity(CONSTRAINT_VIOLATION);
	}

	@ExceptionHandler(CustomException.class)
	protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
		log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
		return ErrorResponse.toResponseEntity(e.getErrorCode());
	}
}
