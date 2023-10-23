package com.example.server.global.oauth;

import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.server.domain.member.model.Member;
import com.example.server.domain.member.model.RoleType;
import com.example.server.domain.member.persistence.MemberRepository;
import com.example.server.global.oauth.provider.KakaoUserInfo;
import com.example.server.global.oauth.provider.NaverUserInfo;
import com.example.server.global.oauth.provider.OAuth2UserInfo;
import com.example.server.global.oauth.provider.constants.OAuth2Provider;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

	private final MemberRepository memberRepository;

	// userRequest 는 code를 받아서 accessToken을 응답 받은 객체
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(userRequest, oAuth2User);
		return new CustomOAuth2User(
			oAuth2UserInfo,
			getOrCreateMember(oAuth2UserInfo)
		);
	}

	private OAuth2UserInfo getOAuth2UserInfo(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		if (registrationId.equals(OAuth2Provider.KAKAO.getProviderName())) {
			return new KakaoUserInfo(oAuth2User.getAttributes());
		} else if (registrationId.equals(OAuth2Provider.NAVER.getProviderName())) {
			return new NaverUserInfo(oAuth2User.getAttributes());
		}
		return null;
	}

	private Member getOrCreateMember(OAuth2UserInfo oAuth2UserInfo) {
		Member existingMember = getExistingMember(oAuth2UserInfo);
		return existingMember != null ? existingMember : createMember(oAuth2UserInfo);
	}

	private Member getExistingMember(OAuth2UserInfo oAuth2UserInfo) {
		Optional<Member> oMember = memberRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(),
			oAuth2UserInfo.getProviderId());
		return oMember.orElse(null); // 회원이 없으면 null 반환
	}

	private Member createMember(OAuth2UserInfo oAuth2UserInfo) {
		Member member = Member.builder()
			.name(oAuth2UserInfo.getName())
			.username(oAuth2UserInfo.getProvider().getProviderName() + "_" + oAuth2UserInfo.getProviderId())
			.email(oAuth2UserInfo.getEmail())
			.mobile(oAuth2UserInfo.getMobile())
			.role(RoleType.ROLE_USER)
			.provider(oAuth2UserInfo.getProvider())
			.providerId(oAuth2UserInfo.getProviderId())
			.build();
		return memberRepository.save(member);
	}
}
