package com.example.server.domain.member.api.dto;

import static lombok.AccessLevel.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.example.server.global.oauth.provider.constants.OAuth2Provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 프로필 조회에 사용되는 dto
 */
@Builder
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class MemberProfileResponse {

	private String name; // Username

	private String username; // Spring Security ID

	private String email;

	private String mobile;

	@Enumerated(EnumType.STRING)
	private OAuth2Provider provider;

	private String providerId;

	private String profileImage;

	private String nickname;

	// private List<PetProfileResponse> petProfileResponses = new ArrayList<>();

}
