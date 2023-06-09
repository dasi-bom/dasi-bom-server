package com.example.server.config.jwt.service;

import com.example.server.config.jwt.dto.RefreshToken;
import com.example.server.config.jwt.repository.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token.accessTokenExpiry}")
    private String accessTokenExpiry;

    @Value("${jwt.token.refreshTokenExpiry}")
    private String refreshTokenExpiry;
    private static final String AUTHORITIES_KEY = "role";
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public JwtService(RefreshTokenRepository refreshTokenRepository){
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String createAccessToken(String socialId, String role) {
        Date expiryDate = getExpiryDate(accessTokenExpiry);
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.builder()
                .setSubject(socialId)
                .claim(AUTHORITIES_KEY, role)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiryDate)
                .compact();
    }

    public String createRefreshToken(String socialId, String role) {
        Date expiryDate = getExpiryDate(refreshTokenExpiry);
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        String jwtToken = Jwts.builder()
                .setSubject(socialId)
                .claim(AUTHORITIES_KEY, role)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiryDate)
                .compact();

        RefreshToken refreshToken = new RefreshToken(jwtToken, socialId);
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getRefreshToken();
    }

    public static Date getExpiryDate(String expiry) {
        return new Date(System.currentTimeMillis() + Long.parseLong(expiry));
    }
}
