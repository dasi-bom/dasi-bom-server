package com.example.server.config.oauth.provider;

/**
 * providerName : OAuth2 인증 요청시 URL 로 들어오는 OAuth2 서비스 제공자명
 * attributeKey : OAuth2 인증 후 계정 정보를 가져올 때 사용하는 키 값
 */
public enum OAuth2Provider {
    KAKAO("kakao", "id"),
    NAVER("naver", "response"),
    GOOGLE("google", "sub");

    private String providerName;
    private String attributeKey;

    OAuth2Provider(String providerName, String attributeKey) {
        this.providerName = providerName;
        this.attributeKey = attributeKey;
    }

    public String getAttributeKey() {
        return this.attributeKey;
    }

    public String getProviderName() {
        return this.providerName;
    }
}
