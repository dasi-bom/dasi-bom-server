package com.example.server.config.jwt;

import com.example.server.config.jwt.dto.AccessToken;
import com.example.server.config.jwt.exception.TokenValidFailedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccessTokenUtil {

//    @Value("${jwt.token.accessTokenExpiry}")
//    private String accessTokenExpiry;
//
//    @Value("${jwt.token.refreshTokenExpiry}")
//    private String refreshTokenExpiry;

    private final Key key;
    private static final String AUTHORITIES_KEY = "role";

    public AccessTokenUtil(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

//    public AuthToken createToken(String id, RoleType roleType, String expiry, Boolean isAccessToken) {
////        Date expiryDate = getExpiryDate(expiry);
////        return new AuthToken(id, roleType, expiryDate, key, isAccessToken);
//        return new AuthToken(id, roleType, expiry, key, isAccessToken);
//    }

//    public String createAccessToken(String id, RoleType roleType, String expiry) {
//        Date expiryDate = getExpiryDate(expiry);
//
//        return Jwts.builder()
//                .setSubject(id)
//                .claim(AUTHORITIES_KEY, roleType.toString())
//                .signWith(key, SignatureAlgorithm.HS256)
//                .setExpiration(expiryDate)
//                .compact();
//    }

//    private String createRefreshToken(String socialId, String role, Date expiry) {
//        String refreshToken = Jwts.builder()
//                .setSubject(socialId)
//                .claim(AUTHORITIES_KEY, role)
//                .signWith(key, SignatureAlgorithm.HS256)
//                .setExpiration(expiry)
//                .compact();
//
//        // redis에 저장
//        redisTemplate.opsForValue().set(
//                authentication.getName(),
//                refreshToken,
//                refreshTokenExpiry,
//                TimeUnit.MILLISECONDS
//        );
//
//        return refreshToken;
//    }

//    public String createUserAppToken(String id) {
//        return createAccessToken(id, RoleType.ROLE_USER, accessTokenExpiry);
//    }
//
//    // 관리자 토큰 생성
//    public String createAdminAppToken(String id) {
//        return createAccessToken(id, RoleType.ROLE_ADMIN, accessTokenExpiry);
//    }

//     사용자 토큰 생성
//    public AuthToken createUserAccessToken(String id) {
//        return createToken(id, RoleType.ROLE_USER, accessTokenExpiry, true);
//    }
//
//    public AuthToken createUserRefreshToken(String id) {
//        return createToken(id, RoleType.ROLE_USER, refreshTokenExpiry, false);
//    }
//
//    // 관리자 토큰 생성
//    public AuthToken createAdminAccessToken(String id) {
//        return createToken(id, RoleType.ROLE_ADMIN, accessTokenExpiry, true);
//    }

    public AccessToken convertAccessToken(String token) {
        return new AccessToken(token, key);
    }

//    public static Date getExpiryDate(String expiry) {
//        return new Date(System.currentTimeMillis() + Long.parseLong(expiry));
//    }

    public Authentication getAuthentication(AccessToken authToken) {

        if (authToken.validate()) {

            Claims claims = authToken.getTokenClaims();
            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(new String[]{claims.get(AUTHORITIES_KEY).toString()})
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            User principal = new User(claims.getSubject(), "", authorities);

            return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
        } else {
            throw new TokenValidFailedException();
        }
    }

}

