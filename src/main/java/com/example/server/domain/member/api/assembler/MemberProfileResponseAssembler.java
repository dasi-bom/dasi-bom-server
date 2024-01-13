package com.example.server.domain.member.api.assembler;

import org.springframework.stereotype.Component;

import com.example.server.domain.image.model.Image;
import com.example.server.domain.member.api.dto.MemberProfileResponse;
import com.example.server.domain.member.model.Member;

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
			// .petProfileResponses(getPetProfileResponses(member))
			.build();
	}

	// private List<PetProfileResponse> getPetProfileResponses(Member member) {
	// 	return member.getPets().stream()
	// 		.map(pet -> PetProfileResponse.builder()
	// 			.petId(pet.getId())
	// 			.providerId(member.getProviderId())
	// 			.petInfo(pet.getPetInfo())
	// 			.petTempProtectedInfo(pet.getPetTempProtectedInfo())
	// 			.isSuccess(true)
	// 			.timestamp(now())
	// 			.imageUrl(getImage(pet.getProfile()))
	// 			.build())
	// 		.collect(Collectors.toList());
	// }

	private String getImage(Image image) {
		return (image != null) ? image.getImgUrl() : null;
	}
}
