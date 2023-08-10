package com.example.server.domain.auth.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class RefreshTokenDao {

	private final RedisTemplate<String, String> redisTemplate;
	private final String REFRESH_TOKEN_HASH_KEY = "refresh-token";

	// save refresh token redis
	public void saveRefreshToken(String providerId, String refreshToken) {
		HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();

		Map<String, String> userMap = new HashMap<>();
		userMap.put(REFRESH_TOKEN_HASH_KEY, refreshToken);

		hashOperations.putAll(providerId, userMap);
		redisTemplate.expire(providerId, 14, TimeUnit.DAYS);
	}

	// get refresh token redis
	public Optional<Object> getRefreshToken(String providerId) {
		HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
		return Optional.ofNullable(hashOperations.get(providerId, REFRESH_TOKEN_HASH_KEY));
	}

	// remove refresh token
	public void removeRefreshToken(String providerId) {
		HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
		hashOperations.delete(providerId, REFRESH_TOKEN_HASH_KEY);
	}

}
