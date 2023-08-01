package com.example.server.global.jwt.exception;

public class TokenValidFailedException extends RuntimeException {

	public TokenValidFailedException() {
		super("Failed to generate Token.");
	}

	public TokenValidFailedException(String message) {
		super(message);
	}
}
