package com.example.server.config.jwt.repository;

import com.example.server.config.jwt.dto.RefreshToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Repository
public class RefreshTokenRepository {

    @Value("${jwt.token.refreshTokenExpiry}")
    private String refreshTokenExpiry;

    private final RedisTemplate redisTemplate;
    public RefreshTokenRepository(final RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public void save(final RefreshToken refreshToken){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(
                refreshToken.getRefreshToken(),
                refreshToken.getMemberId()
        );

//        redisTemplate.opsForValue().set(
//                refreshToken.getMemberId(),
//                refreshToken.getRefreshToken(),
//                Long.parseLong(refreshToken.getExpiry()),
//                TimeUnit.MILLISECONDS
//        );
//        redisTemplate.expire(refreshToken.getRefreshToken(), 60L, TimeUnit.SECONDS);

        redisTemplate.expire(refreshToken.getRefreshToken(), Long.parseLong(refreshTokenExpiry), TimeUnit.MILLISECONDS);
    }

    public Optional<RefreshToken> findById(final String refreshToken){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String userIdx = valueOperations.get(refreshToken);

        if(Objects.isNull(userIdx)){
            return Optional.empty();
        }
        return Optional.of(new RefreshToken(refreshToken, userIdx));
    }
}
