package com.example.server.global.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

	public static <T> ResponseEntity<T> success(T body) {
		return ResponseEntity.status(HttpStatus.OK).body(body);
	}

	public static <T> ResponseEntity<T> created(T body) {
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}

	public static <T> ResponseEntity<T> noContent(T body) {
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(body);
	}

	public static <T> ResponseEntity<T> fail(T body) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	}

	public static <T> ResponseEntity<T> forbidden(T body) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
	}
}
