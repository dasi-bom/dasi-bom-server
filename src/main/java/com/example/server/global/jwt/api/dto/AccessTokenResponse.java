package com.example.server.global.jwt.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenResponse {

	private String accessToken;

	public static AccessTokenResponse of(String accessToken) {
		return AccessTokenResponse.builder()
			.accessToken(accessToken)
			.build();
	}
}
