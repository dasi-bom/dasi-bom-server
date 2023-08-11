package com.example.server.global.jwt;

import static com.example.server.global.exception.ErrorCode.*;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.example.server.domain.member.model.constants.RoleType;
import com.example.server.global.exception.BusinessException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TokenProvider {

	private static final String AUTHORITIES_KEY = "role";
	private final Key key;
	@Value("${jwt.token.access-token-expiry}")
	private String accessTokenExpiry;
	@Value("${jwt.token.refresh-token-expiry}")
	private String refreshTokenExpiry;

	public TokenProvider(String secretKey) {
		this.key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(secretKey.getBytes()));
	}

	public AuthToken convertAuthToken(String token) {
		return new AuthToken(token, key);
	}

	public AuthToken generateToken(String providerId, String role, boolean isAccessToken) {

		Claims claims = Jwts.claims().setSubject(providerId);
		claims.put(AUTHORITIES_KEY, role);

		Long duration = isAccessToken ? Long.parseLong(accessTokenExpiry) : Long.parseLong(refreshTokenExpiry);

		return AuthToken.builder()
			.claims(claims)
			.key(key)
			.duration(duration)
			.build();
	}

	public Authentication getAuthentication(AuthToken authToken) {

		if (!authToken.validateToken()) {
			throw new BusinessException(ACCESS_TOKEN_INVALID);
		}

		Claims claims = authToken.getTokenClaims();
		Collection<? extends GrantedAuthority> authorities = getGrantedAuthorities(claims);
		User principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
	}

	private Collection<? extends GrantedAuthority> getGrantedAuthorities(Claims claims) {

		RoleType role = RoleType.valueOf((String)claims.get("role"));

		return Arrays.stream(new String[] {claims.get(AUTHORITIES_KEY).toString()})
			.map(c -> new SimpleGrantedAuthority(role.toString()))
			.collect(Collectors.toList());
	}

	/**
	 * http 헤더로부터 bearer 토큰을 가져옴.
	 */
	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
