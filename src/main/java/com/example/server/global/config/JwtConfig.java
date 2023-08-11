package com.example.server.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.server.global.jwt.TokenProvider;

@Configuration
public class JwtConfig {

	@Value("${access-token.secret}")
	private String accessTokenSecret;
	@Value("${refresh-token.secret}")
	private String refreshTokenSecret;

	@Bean
	public TokenProvider jwtProvider() {
		return new TokenProvider(accessTokenSecret, refreshTokenSecret);
	}
}
