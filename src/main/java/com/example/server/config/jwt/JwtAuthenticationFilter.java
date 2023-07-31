package com.example.server.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.server.config.jwt.dto.AccessToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * 클라이언트에게 받은 JWT 가 유효한 토큰인지 확인하는 "인가 필터"
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final AccessTokenUtil tokenProvider;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		// 1. Request Header 에서 JWT 토큰 추출
		final String authorizationHeader = request.getHeader("Authorization");

		// 2. validateToken 으로 토큰 유효성 검사
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String tokenStr = JwtHeaderUtil.getAccessToken(request);
			AccessToken token = tokenProvider.convertAccessToken(tokenStr);
			try {
				if (token.validate()) {
					Authentication authentication = tokenProvider.getAuthentication(token);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			} catch (RedisConnectionFailureException e) {
				SecurityContextHolder.clearContext();
				e.printStackTrace();
				//                throw new BaseException(REDIS_ERROR);
			} catch (Exception e) {
				e.printStackTrace();
				//                throw new BaseException(INVALID_JWT);
			}
		} else {
			SecurityContextHolder.getContext().setAuthentication(null);
		}
		filterChain.doFilter(request, response);
	}
}
