package com.example.server.pet.support;

import com.example.server.domain.pet.api.dto.PetProfileCreateRequest;
import com.example.server.domain.pet.model.PetInfo;
import com.example.server.domain.pet.model.constants.PetSex;
import com.example.server.domain.pet.model.constants.PetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.example.server.domain.pet.model.constants.PetSex.MALE;
import static com.example.server.domain.pet.model.constants.PetType.FOX;

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
        return PetInfo.of(
                name,
                age,
                type,
                sex,
                bio
        );
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
