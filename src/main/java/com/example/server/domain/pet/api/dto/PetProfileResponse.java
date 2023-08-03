package com.example.server.domain.pet.api.dto;

import static java.time.LocalDateTime.*;
import static lombok.AccessLevel.*;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class PetProfileResponse {

	Long petId;
	String petName;
	Boolean isSuccess;
	LocalDateTime timestamp;

	//== static factory method ==//
	public static PetProfileResponse of(
		Long petId,
		String petName
	) {
		return PetProfileResponse.builder()
			.petId(petId)
			.petName(petName)
			.isSuccess(true)
			.timestamp(now())
			.build();
	}
}
