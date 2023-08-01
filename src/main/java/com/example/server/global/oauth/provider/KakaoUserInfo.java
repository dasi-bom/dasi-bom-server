package com.example.server.global.oauth.provider;

import java.util.Map;
import java.util.Optional;

public class KakaoUserInfo extends OAuth2UserInfo {

	public KakaoUserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getProviderId() {
		return String.valueOf(attributes.get("id"));
	}

	@Override
	public OAuth2Provider getProvider() {
		return OAuth2Provider.KAKAO;
	}

	@Override
	public String getEmail() {
		return Optional.ofNullable(attributes.get("kakao_account"))
			.map(account -> (String)((Map<String, Object>)account).get("email"))
			.orElse(null);
	}

	@Override
	public String getName() {
		return Optional.ofNullable(attributes.get("kakao_account"))
			.map(account -> ((Map<String, Object>)account).get("profile"))
			.map(profile -> (String)((Map<String, Object>)profile).get("nickname"))
			.orElse(null);
	}

	@Override
	public String getMobile() {
		return Optional.ofNullable(attributes.get("kakao_account"))
			.map(account -> (String)((Map<String, Object>)account).get("phone_number"))
			.orElse(null);
	}

}
