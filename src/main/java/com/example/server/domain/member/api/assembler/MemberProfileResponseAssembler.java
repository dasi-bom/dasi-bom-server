package com.example.server.domain.member.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.server.domain.image.model.Image;
import com.example.server.domain.member.api.dto.MemberProfileResponse;
import com.example.server.domain.member.model.Member;
import com.example.server.domain.pet.api.dto.PetProfileResponse;

@Component
public class MemberProfileResponseAssembler {
	public MemberProfileResponse toResponse(Member member) {
		return MemberProfileResponse.builder()
			.name(member.getName())
			.username(member.getUsername())
			.email(member.getEmail())
			.mobile(member.getMobile())
			.provider(member.getProvider())
			.providerId(member.getProviderId())
			.profileImage(getImage(member.getProfileImage()))
			.nickname(member.getNickname())
			.petProfileResponses(getPetProfileResponses(member))
			.build();
	}

	private static List<PetProfileResponse> getPetProfileResponses(Member member) {
		return member.getPets().stream()
			.map(pet -> PetProfileResponse.of(pet.getId(), member.getProviderId(), pet.getPetInfo(),
				pet.getPetTempProtectedInfo()))
			.collect(Collectors.toList());
	}

	private static String getImage(Image image) {
		return (image != null) ? image.getImgUrl() : null;
	}
}
