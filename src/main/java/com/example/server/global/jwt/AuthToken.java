package com.example.server.global.jwt;

import static com.example.server.global.exception.ErrorCode.*;

import java.security.Key;
import java.util.Date;

import com.example.server.global.exception.BusinessException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AuthToken {

	@Getter
	private final String token;

	private final Key key;

	@Builder
	public AuthToken(Claims claims, Long duration, Key key) {
		this.key = key;
		this.token = createAuthToken(claims, duration);
	}

	private String createAuthToken(Claims claims, Long duration) {
		Date now = new Date();
		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + duration))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public boolean validateToken() {

		return this.getTokenClaims() != null;
	}

	public Claims getTokenClaims() {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();

		} catch (ExpiredJwtException expiredJwtException) {
			throw expiredJwtException;
		} catch (Exception exception) {
			throw new BusinessException(ACCESS_TOKEN_INVALID);
		}
	}

	/**
	 * 토큰 만료여부와 관계없이 subject 인 providerId get
	 */
	public String getProviderId() {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
		} catch (ExpiredJwtException expiredJwtException) {

			return expiredJwtException.getClaims().getSubject();
		}
	}
}
