package com.example.server.domain.pet.api.dto;

import static lombok.AccessLevel.*;

import java.time.LocalDateTime;

import com.example.server.domain.pet.model.PetInfo;
import com.example.server.domain.pet.model.PetTempProtectedInfo;

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
	String providerId;
	PetInfo petInfo;
	PetTempProtectedInfo petTempProtectedInfo;
	Boolean isSuccess;
	LocalDateTime timestamp;

}
