package com.example.server.domain.member.api.dto;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 가입 후 최초 1회, 프로필 등록 완료 시 사용되는 dto
 * 동물 프로필까지 등록했을 때에만 사용
 */
@Builder
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class FirstProfileCreateResponse {

	private String name; // Username
	private String memberProfileImage;
	private String nickname;
	private String petName;
	private String petProfileImage;

	//== static factory method ==//
	public static FirstProfileCreateResponse of(
		String name,
		String memberProfileImage,
		String nickname,
		String petName,
		String petProfileImage
	) {
		return FirstProfileCreateResponse.builder()
			.name(name)
			.memberProfileImage(memberProfileImage)
			.nickname(nickname)
			.petName(petName)
			.petProfileImage(petProfileImage)
			.build();
	}
}
