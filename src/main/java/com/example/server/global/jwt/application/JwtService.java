package com.example.server.global.jwt.application;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.server.domain.member.model.RoleType;
import com.example.server.global.exception.BusinessException;
import com.example.server.global.exception.errorcode.AuthErrorCode;
import com.example.server.global.jwt.AuthToken;
import com.example.server.global.jwt.TokenProvider;
import com.example.server.global.jwt.api.dto.AccessTokenRequest;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

	private final TokenProvider tokenProvider;
	private final RefreshTokenService refreshTokenService;

	/**
	 * access token 만료된 경우, access token 재발급
	 */
	public String getAccessToken(AccessTokenRequest accessTokenRequest) {

		String accessTokenValue = accessTokenRequest.getAccessToken();

		AuthToken accessToken = tokenProvider.convertAccessToken(accessTokenValue);
		String providerId = accessToken.getProviderId();

		/**
		 * access token 을 validate 한 후, 이상없으면 token 을 내려준다.
		 * access token 이 존재하지만, 만료된 경우에는 reissue 한다.
		 */
		try {
			accessToken.validateToken();
			return accessTokenValue;
		} catch (ExpiredJwtException expiredJwtException) {
			return reIssueAccessTokenFromRefreshToken(providerId);
		}
	}

	/**
	 * redis 의 refresh token 을 삭제하는 메소드
	 */
	public void removeToken(UserDetails userDetails) {
		String providerId = userDetails.getUsername();
		refreshTokenService.delete(providerId);
	}

	/**
	 * refresh token 으로부터 access token 을 재발급 받는 메소드
	 */
	private String reIssueAccessTokenFromRefreshToken(String providerId) {

		String refreshTokenValue = refreshTokenService.findById(providerId);
		AuthToken refreshToken = tokenProvider.convertRefreshToken(refreshTokenValue);
		checkRefreshTokenValidation(refreshToken, refreshTokenValue, providerId);

		AuthToken accessToken = tokenProvider.generateToken(providerId, RoleType.ROLE_USER.name(), true);

		return accessToken.getToken();
	}

	/**
	 * refresh token validation 메소드
	 */
	private void checkRefreshTokenValidation(AuthToken refreshToken, String refreshTokenValue, String providerId) {
		try {
			tokenProvider.convertRefreshToken(refreshTokenValue).validateToken();

			if (!providerId.equals(refreshToken.getProviderId())) {
				throw new BusinessException(AuthErrorCode.ACCESS_TOKEN_INVALID);
			}

		} catch (ExpiredJwtException expiredJwtException) {
			throw new BusinessException(AuthErrorCode.REFRESH_TOKEN_EXPIRED);
		}
	}

}
