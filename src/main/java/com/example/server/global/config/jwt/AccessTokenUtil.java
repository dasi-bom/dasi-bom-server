package com.example.server.global.config.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.example.server.global.config.jwt.dto.AccessToken;
import com.example.server.global.config.jwt.exception.TokenValidFailedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AccessTokenUtil {

	private static final String AUTHORITIES_KEY = "role";
	private final Key key;

	public AccessTokenUtil(@Value("${jwt.secret}") String secretKey) {
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	public AccessToken convertAccessToken(String token) {
		return new AccessToken(token, key);
	}

	public Authentication getAuthentication(AccessToken authToken) {

		if (authToken.validate()) {

			Claims claims = authToken.getTokenClaims();
			Collection<? extends GrantedAuthority> authorities =
				Arrays.stream(new String[] {claims.get(AUTHORITIES_KEY).toString()})
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());

			User principal = new User(claims.getSubject(), "", authorities);

			return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
		} else {
			throw new TokenValidFailedException();
		}
	}

}

