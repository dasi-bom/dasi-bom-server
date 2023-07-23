package com.example.server.config.oauth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.server.config.oauth.provider.KakaoUserInfo;
import com.example.server.config.oauth.provider.NaverUserInfo;
import com.example.server.config.oauth.provider.OAuth2Provider;
import com.example.server.config.oauth.provider.OAuth2UserInfo;
import com.example.server.domain.Member;
import com.example.server.domain.RoleType;
import com.example.server.repository.MemberRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

	@Autowired
	private MemberRepository memberRepository;

	// userRequest 는 code를 받아서 accessToken을 응답 받은 객체
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		return processOAuth2User(userRequest, oAuth2User);
	}

	private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
		OAuth2UserInfo oAuth2UserInfo = null;

		// OAuth2 서비스 제공자 구분
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		if (registrationId.equals(OAuth2Provider.KAKAO.getProviderName())) {
			oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
		} else if (registrationId.equals(OAuth2Provider.NAVER.getProviderName())) {
			oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
		}

		Optional<Member> userOptional =
			memberRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());

		Member user;
		boolean isFirst; // 최초 로그인 여부
		if (userOptional.isPresent()) { // 이미 가입한 회원이라면
			isFirst = false;
			user = userOptional.get();
			user.setEmail(oAuth2UserInfo.getEmail());
			memberRepository.save(user);
		} else {
			isFirst = true;
			// user의 패스워드가 null이기 때문에 OAuth 유저는 일반적인 로그인을 할 수 없음.
			user = Member.builder()
				.name(oAuth2UserInfo.getName())
				.username(oAuth2UserInfo.getProvider().getProviderName() + "_" + oAuth2UserInfo.getProviderId())
				.email(oAuth2UserInfo.getEmail())
				.mobile(oAuth2UserInfo.getMobile())
				.role(RoleType.ROLE_USER)
				.provider(oAuth2UserInfo.getProvider())
				.providerId(oAuth2UserInfo.getProviderId())
				.build();
			memberRepository.save(user);
		}

		return new CustomOAuth2User(oAuth2UserInfo, user, isFirst);
	}
}
