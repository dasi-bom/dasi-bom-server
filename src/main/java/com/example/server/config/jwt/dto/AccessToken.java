package com.example.server.config.jwt.dto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.security.Key;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AccessToken {

    @Getter
    private final String token;

    private final Key key;

//    @Getter
//    private String token;
//    private Key key;

//    private static final String AUTHORITIES_KEY = "role";

//    private final RefreshTokenRepository refreshTokenRepository;

//    private final RedisTemplate<String, String> redisTemplate;
//
//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;

//    public AuthToken(String token, Key key, RedisTemplate<String, String> redisTemplate){
//        this.token = token;
//        this.key = key;
//        this.redisTemplate = redisTemplate;
//    }
//
//    public AuthToken(String token, Key key) {
//        this.token = token;
//        this.key = key;
//    }

//    AuthToken(String socialId, RoleType roleType, String expiry, Key key, Boolean isAccessToken) {
//        String role = roleType.toString();
//        Date expiryDate = AuthTokenProvider.getExpiryDate(expiry);
//        this.key = key;
//        if (isAccessToken) {
//            this.token = createAccessToken(socialId, role, expiryDate);
//        } else {
//            this.token = createRefreshToken(socialId, role, expiryDate, expiry);
////            this.token = createRefreshToken(socialId, role, expiryDate, expiry);
//        }
//    }
//
//    private String createAccessToken(String socialId, String role, Date expiryDate) {
//        return Jwts.builder()
//                .setSubject(socialId)
//                .claim(AUTHORITIES_KEY, role)
//                .signWith(key, SignatureAlgorithm.HS256)
//                .setExpiration(expiryDate)
//                .compact();
//    }
//
//    private String createRefreshToken(String socialId, String role, Date expiryDate, String expiry) {
//        String refreshToken = Jwts.builder()
//                .setSubject(socialId)
//                .claim(AUTHORITIES_KEY, role)
//                .signWith(key, SignatureAlgorithm.HS256)
//                .setExpiration(expiryDate)
//                .compact();
//
////        RefreshToken refreshToken = new RefreshToken(jwtToken, socialId);
////        refreshTokenRepository.save(refreshToken);
////        return refreshToken.getRefreshToken();
////
//        System.out.println("======> refreshToken: " + refreshToken);
//        System.out.println("======> redisTemplate: " + redisTemplate);
//
//        // redis에 저장
//        redisTemplate.opsForValue().set(
////                authentication.getName(),
//                socialId,
//                refreshToken,
//                Long.parseLong(expiry),
//                TimeUnit.MILLISECONDS
//        );
//
//        return refreshToken;
//    }

    public boolean validate() {
        return this.getTokenClaims() != null;
    }

    public Claims getTokenClaims() {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return null;
    }
}
