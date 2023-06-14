package com.example.server.config.oauth.provider;

import java.util.Map;

public class KakaoUserInfo extends OAuth2UserInfo{

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
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        if (account == null) {
            return null;
        }
        return (String) account.get("email");
    }

    @Override
    public String getName() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");
        if (account == null || profile == null) {
            return null;
        }
        return (String) profile.get("nickname");
    }



//	private Map<String, Object> attributes;
//
//    public KakaoUserInfo(Map<String, Object> attributes) {
//        this.attributes = attributes;
//    }
//
//    @Override
//    public String getProviderId() {
//        return (String) attributes.get("id");
//    }
//
//    @Override
//    public String getName() {
//        return (String) attributes.get("username");
//    }
//
//    @Override
//    public String getEmail() {
//        return (String) attributes.get("email");
//    }
//
//	@Override
//	public String getProvider() {
//		return "kakao";
//	}

}
