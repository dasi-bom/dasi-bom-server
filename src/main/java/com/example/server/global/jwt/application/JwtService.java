package com.example.server.global.jwt.application;

import static com.example.server.global.exception.ErrorCode.*;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.server.domain.member.model.constants.RoleType;
import com.example.server.global.exception.BusinessException;
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

		AuthToken accessToken = tokenProvider.convertAuthToken(accessTokenValue);
		String providerId = accessToken.getProviderId();

		/**
		 * access token을 validate 한 후, 이상없으면 token을 내려준다.
		 * access token이 존재하지만, 만료된 경우에는 reissue한다.
		 */
		try {
			accessToken.validateToken();
			return accessTokenValue;
		} catch (ExpiredJwtException expiredJwtException) {
			return reIssueAccessTokenFromRefreshToken(providerId);
		}
	}

	/**
	 * refresh token 으로부터 access token 을 재발급 받는 메소드
	 */
	public String reIssueAccessTokenFromRefreshToken(String providerId) {

		String refreshTokenValue = refreshTokenService.findById(providerId);
		AuthToken refreshToken = tokenProvider.convertAuthToken(refreshTokenValue);
		checkRefreshTokenValidation(refreshToken, refreshTokenValue, providerId);

		AuthToken accessToken = tokenProvider.generateToken(providerId, RoleType.ROLE_USER.name(), true);

		return accessToken.getToken();
	}

	/**
	 * redis 의 refresh token 을 삭제하는 메소드
	 */
	public void removeToken(UserDetails userDetails) {
		String providerId = userDetails.getUsername();
		refreshTokenService.delete(providerId);
	}

	/**
	 * refresh token validation 메소드
	 */
	private void checkRefreshTokenValidation(AuthToken refreshToken, String refreshTokenValue, String providerId) {
		try {
			tokenProvider.convertAuthToken(refreshTokenValue).validateToken();

			if (!providerId.equals(refreshToken.getProviderId())) {
				throw new BusinessException(ACCESS_TOKEN_INVALID);
			}

		} catch (ExpiredJwtException expiredJwtException) {
			throw new BusinessException(REFRESH_TOKEN_EXPIRED);
		}
	}

}
