package com.example.server.global.jwt;

import static com.example.server.global.exception.ErrorCode.*;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

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
	private final Long ACCESS_TOKEN_PERIOD = 1000L * 60L * 120L; // 2시간
	private final Long REFRESH_TOKEN_ERIOD = 1000L * 60L * 60L * 24L * 14L; // 14일

	public TokenProvider(String secretKey) {
		this.key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(secretKey.getBytes()));
	}

	public AuthToken convertAuthToken(String token) {
		return new AuthToken(token, key);
	}

	public AuthToken generateToken(String providerId, String role, boolean isAccessToken) {

		Claims claims = Jwts.claims().setSubject(providerId);
		claims.put(AUTHORITIES_KEY, role);

		Long duration = isAccessToken ? ACCESS_TOKEN_PERIOD : REFRESH_TOKEN_ERIOD;

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
