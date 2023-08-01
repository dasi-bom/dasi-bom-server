package com.example.server.global.oauth.provider;

import java.util.Map;
import java.util.Optional;

import com.example.server.global.oauth.provider.constants.OAuth2Provider;

public class NaverUserInfo extends OAuth2UserInfo {

	public NaverUserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getProviderId() {
		return Optional.ofNullable(attributes.get("response"))
			.map(account -> (String)((Map<String, Object>)account).get("id"))
			.orElse(null);
	}

	@Override
	public OAuth2Provider getProvider() {
		return OAuth2Provider.NAVER;
	}

	@Override
	public String getEmail() {
		return Optional.ofNullable(attributes.get("response"))
			.map(account -> (String)((Map<String, Object>)account).get("email"))
			.orElse(null);
	}

	@Override
	public String getName() {
		return Optional.ofNullable(attributes.get("response"))
			.map(account -> (String)((Map<String, Object>)account).get("name"))
			.orElse(null);
	}

	@Override
	public String getMobile() {
		return Optional.ofNullable(attributes.get("response"))
			.map(account -> (String)((Map<String, Object>)account).get("mobile"))
			.orElse(null);
	}

}
