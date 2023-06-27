package com.example.server.config.jwt;

import com.example.server.config.jwt.dto.AuthResponse;
import com.example.server.config.oauth.CustomOAuth2User;
import com.example.server.config.oauth.provider.KakaoUserInfo;
import com.example.server.config.oauth.provider.NaverUserInfo;
import com.example.server.config.oauth.provider.OAuth2Provider;
import com.example.server.config.oauth.provider.OAuth2UserInfo;
import com.example.server.domain.Member;
import com.example.server.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.util.logging.Slf4j;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/*
 * 요청으로 준 유저정보와 DB 에 담긴 유저정보가 일치할 때 JWT 토큰을 생성하는 필터
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthTokenProvider authTokenProvider;
    private final MemberRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        writeTokenResponse(response, authentication);
    }

    private void writeTokenResponse(HttpServletResponse response, Authentication authentication) throws IOException {

        CustomOAuth2User oauth2user = (CustomOAuth2User) authentication.getPrincipal();
        String userName = oauth2user.getName(); //authentication 의 name

        Optional<Member> oUser = userRepository.findById(Integer.parseInt(userName));

        String providerName = oauth2user.getProviderName(); //authentication 의 name
        OAuth2UserInfo oAuth2UserInfo = null;
        if (providerName.equals(OAuth2Provider.KAKAO.getProviderName())) {
            oAuth2UserInfo = new KakaoUserInfo(oauth2user.getAttributes());
        } else if (providerName.equals(OAuth2Provider.NAVER.getProviderName())) {
            oAuth2UserInfo = new NaverUserInfo(oauth2user.getAttributes());
        }

        // JWT 생성
        AuthToken appToken = authTokenProvider.createUserAppToken(oAuth2UserInfo.getProviderId());

        AuthResponse authResponse;
        if (oUser.isEmpty()) {
            authResponse = AuthResponse.builder()
                    .appToken(appToken.getToken())
                    .isNewMember(Boolean.TRUE)
                    .build();
        } else {
            authResponse = AuthResponse.builder()
                    .appToken(appToken.getToken())
                    .isNewMember(Boolean.FALSE)
                    .build();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), authResponse);

    }

}
