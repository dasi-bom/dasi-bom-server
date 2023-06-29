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

//    public String[] createTokenWhenLogin(int userIdx){
//        String refreshToken = createRefreshToken(userIdx);
//        String accessToken = createAccessToken(userIdx);
//
//        String[] tokenList = {refreshToken, accessToken};
//        return tokenList;
//    }

    public String createAccessToken(String socialId, String role) {
//        byte[] keyBytes = Decoders.BASE64.decode(secret);
//        Key key = Keys.hmacShaKeyFor(keyBytes);
        Date expiryDate = getExpiryDate(accessTokenExpiry);
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.builder()
                .setSubject(socialId)
                .claim(AUTHORITIES_KEY, role)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiryDate)
                .compact();
    }

//    public String createAccessToken(int userIdx){
//        byte[] keyBytes = Decoders.BASE64.decode(Secret.ACCESS_TOKEN_SECRET_KEY);
//        Key key = Keys.hmacShaKeyFor(keyBytes);
//        Date now = new Date();
//        return Jwts.builder()
//                .setHeaderParam("type","jwt")
//                .claim("userIdx",userIdx)
//                .setIssuedAt(now)
//                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*30))) // 만료기간은 30분으로 설정
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//    }

    public String createRefreshToken(String socialId, String role) {
//        byte[] keyBytes = Decoders.BASE64.decode(secret);
//        Key key = Keys.hmacShaKeyFor(keyBytes);
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

//    public String getAccessToken(){
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//        return request.getHeader("Authorization");
//    }
//
//    public String getRefreshToken(){
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//        return request.getHeader("RefreshToken");
//    }
}
