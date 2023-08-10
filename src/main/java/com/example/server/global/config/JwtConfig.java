package com.example.server.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.server.global.jwt.TokenProvider;

@Configuration
public class JwtConfig {

	@Value("${jwt.secret}")
	private String secretKey;

	@Bean
	public TokenProvider jwtProvider() {
		return new TokenProvider(secretKey);
	}
}
