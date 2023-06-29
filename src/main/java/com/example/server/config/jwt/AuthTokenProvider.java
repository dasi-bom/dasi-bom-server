package com.example.server.config.jwt;

import com.example.server.config.jwt.exception.TokenValidFailedException;
import com.example.server.domain.RoleType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
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
public class AuthTokenProvider {

    @Value("${jwt.token.accessTokenExpiry}")
    private String expiry;

    private final Key key;
    private static final String AUTHORITIES_KEY = "role";

    public AuthTokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public AuthToken createToken(String id, RoleType roleType, String expiry) {
        Date expiryDate = getExpiryDate(expiry);
        return new AuthToken(id, roleType, expiryDate, key);
    }

    // 사용자 토큰 생성
    public AuthToken createUserAppToken(String id) {
        return createToken(id, RoleType.ROLE_USER, expiry);
    }

    // 관리자 토큰 생성
    public AuthToken createAdminAppToken(String id) {
        return createToken(id, RoleType.ROLE_ADMIN, expiry);
    }

    public AuthToken convertAuthToken(String token) {
        return new AuthToken(token, key);
    }

    public static Date getExpiryDate(String expiry) {
        return new Date(System.currentTimeMillis() + Long.parseLong(expiry));
    }

    public Authentication getAuthentication(AuthToken authToken) {

        if(authToken.validate()) {

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

