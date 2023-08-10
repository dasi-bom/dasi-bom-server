// package com.example.server.global.jwt.service;
//
// import java.security.Key;
// import java.util.Date;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
//
// import com.example.server.global.jwt.dto.RefreshToken;
// import com.example.server.global.jwt.repository.RefreshTokenRepository;
//
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.security.Keys;
//
// @Service
// public class JwtService {
//
// 	private static final String AUTHORITIES_KEY = "role";
// 	private final RefreshTokenRepository refreshTokenRepository;
// 	@Value("${jwt.secret}")
// 	private String secret;
// 	@Value("${jwt.token.accessTokenExpiry}")
// 	private String accessTokenExpiry;
// 	@Value("${jwt.token.refreshTokenExpiry}")
// 	private String refreshTokenExpiry;
//
// 	@Autowired
// 	public JwtService(RefreshTokenRepository refreshTokenRepository) {
// 		this.refreshTokenRepository = refreshTokenRepository;
// 	}
//
// 	public static Date getExpiryDate(String expiry) {
// 		return new Date(System.currentTimeMillis() + Long.parseLong(expiry));
// 	}
//
// 	public String createAccessToken(String socialId, String role) {
// 		Date expiryDate = getExpiryDate(accessTokenExpiry);
// 		Key key = Keys.hmacShaKeyFor(secret.getBytes());
// 		return Jwts.builder()
// 			.setSubject(socialId)
// 			.claim(AUTHORITIES_KEY, role)
// 			.signWith(key, SignatureAlgorithm.HS256)
// 			.setExpiration(expiryDate)
// 			.compact();
// 	}
//
// 	public String createRefreshToken(String socialId, String role) {
// 		Date expiryDate = getExpiryDate(refreshTokenExpiry);
// 		Key key = Keys.hmacShaKeyFor(secret.getBytes());
// 		String jwtToken = Jwts.builder()
// 			.setSubject(socialId)
// 			.claim(AUTHORITIES_KEY, role)
// 			.signWith(key, SignatureAlgorithm.HS256)
// 			.setExpiration(expiryDate)
// 			.compact();
//
// 		RefreshToken refreshToken = new RefreshToken(jwtToken, socialId);
// 		refreshTokenRepository.save(refreshToken);
// 		return refreshToken.getRefreshToken();
// 	}
// }
