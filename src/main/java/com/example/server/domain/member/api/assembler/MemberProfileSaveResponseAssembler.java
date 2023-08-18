package com.example.server.domain.member.api.assembler;

import org.springframework.stereotype.Component;

import com.example.server.domain.image.model.Image;
import com.example.server.domain.member.api.dto.MemberProfileSaveResponse;
import com.example.server.domain.member.model.Member;

@Component
public class MemberProfileSaveResponseAssembler {
	public MemberProfileSaveResponse toResponse(Member member) {
		return MemberProfileSaveResponse.builder()
			.name(member.getName())
			.username(member.getUsername())
			.email(member.getEmail())
			.mobile(member.getMobile())
			.provider(member.getProvider())
			.providerId(member.getProviderId())
			.profileImage(getImage(member.getProfileImage()))
			.nickname(member.getNickname())
			.build();
	}

	private static String getImage(Image image) {
		return (image != null) ? image.getImgUrl() : null;
	}
}
