package com.example.server.global.oauth.handler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.server.domain.member.model.RoleType;
import com.example.server.global.jwt.AuthToken;
import com.example.server.global.jwt.TokenProvider;
import com.example.server.global.jwt.application.RefreshTokenService;
import com.example.server.global.oauth.CustomOAuth2User;
import com.example.server.global.oauth.provider.KakaoUserInfo;
import com.example.server.global.oauth.provider.NaverUserInfo;
import com.example.server.global.oauth.provider.OAuth2UserInfo;
import com.example.server.global.oauth.provider.constants.OAuth2Provider;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;

/*
 * 요청으로 준 유저정보와 DB 에 담긴 유저정보가 일치할 때 JWT 토큰을 생성하는 필터
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private static final String SUCCESS_LOGIN = "auth/success-login"; // /auth 아니고 auth
	private static final String TOKEN = "token";
	private final TokenProvider tokenProvider;
	private final RefreshTokenService refreshTokenService;
	@Value("${callback-url-scheme}")
	private String callbackUrlScheme;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		CustomOAuth2User oauth2user = (CustomOAuth2User)authentication.getPrincipal();
		OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(oauth2user);
		AuthToken accessToken = generateAuthToken(oAuth2UserInfo);
		String targetUrl = createTargetUrl(accessToken.getToken());
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	private AuthToken generateAuthToken(OAuth2UserInfo oAuth2UserInfo) {
		AuthToken accessToken = tokenProvider.generateToken(oAuth2UserInfo.getProviderId(), RoleType.ROLE_USER.name(),
			true);
		AuthToken refreshToken = tokenProvider.generateToken(oAuth2UserInfo.getProviderId(), RoleType.ROLE_USER.name(),
			false);
		refreshTokenService.save(oAuth2UserInfo.getProviderId(), refreshToken.getToken());
		return accessToken;
	}

	private OAuth2UserInfo getOAuth2UserInfo(CustomOAuth2User oauth2user) {
		String providerName = oauth2user.getProviderName();
		if (providerName.equals(OAuth2Provider.KAKAO.getProviderName())) {
			return new KakaoUserInfo(oauth2user.getAttributes());
		} else if (providerName.equals(OAuth2Provider.NAVER.getProviderName())) {
			return new NaverUserInfo(oauth2user.getAttributes());
		}
		throw new IllegalArgumentException("Unsupported OAuth2 provider: " + providerName);
	}

	private String createTargetUrl(String accessToken) throws UnsupportedEncodingException {
		return UriComponentsBuilder
			.fromUriString(callbackUrlScheme + SUCCESS_LOGIN)
			.queryParam(TOKEN, accessToken)
			.build().toUriString();
	}

}
