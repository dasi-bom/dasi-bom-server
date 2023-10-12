package com.example.server.pet.support;

import static com.example.server.domain.pet.model.constants.PetSex.*;
import static com.example.server.domain.pet.model.constants.PetType.*;

import com.example.server.domain.pet.api.dto.PetProfileCreateRequest;
import com.example.server.domain.pet.model.PetInfo;
import com.example.server.domain.pet.model.constants.PetSex;
import com.example.server.domain.pet.model.constants.PetType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public enum PetInfoFixture {

	VALID_PET("체리", 10, FOX, MALE, "hello! I'm 10 years Old, Wild fox!");

	private String name;
	private Integer age;
	private PetType type;
	private PetSex sex;
	private String bio;

	public PetInfo toEntity() {
		return PetInfo.builder()
			.name(name)
			.age(age)
			.type(type)
			.sex(sex)
			.bio(bio)
			.build();
	}

	public PetProfileCreateRequest toRequest() {
		return PetProfileCreateRequest.builder()
			.name(name)
			.age(age)
			.type(type.toString())
			.sex(sex.toString())
			.bio(bio)
			.build();
	}
}
