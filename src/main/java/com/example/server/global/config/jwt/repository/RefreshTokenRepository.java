package com.example.server.global.config.jwt.repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.example.server.global.config.jwt.dto.RefreshToken;

@Repository
public class RefreshTokenRepository {

	private final RedisTemplate redisTemplate;
	@Value("${jwt.token.refreshTokenExpiry}")
	private String refreshTokenExpiry;

	public RefreshTokenRepository(final RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void save(final RefreshToken refreshToken) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(
			refreshToken.getRefreshToken(),
			refreshToken.getMemberId()
		);

		redisTemplate.expire(refreshToken.getRefreshToken(), Long.parseLong(refreshTokenExpiry), TimeUnit.MILLISECONDS);
	}

	public Optional<RefreshToken> findById(final String refreshToken) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		String userIdx = valueOperations.get(refreshToken);

		if (Objects.isNull(userIdx)) {
			return Optional.empty();
		}
		return Optional.of(new RefreshToken(refreshToken, userIdx));
	}
}
