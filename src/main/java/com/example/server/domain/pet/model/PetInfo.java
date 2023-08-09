package com.example.server.domain.pet.model;

import com.example.server.domain.pet.model.constants.PetSex;
import com.example.server.domain.pet.model.constants.PetType;
import com.example.server.global.exception.BusinessException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;

import static com.example.server.domain.pet.model.constants.PetType.ERROR;
import static com.example.server.global.exception.ErrorCode.*;
import static java.util.Objects.isNull;
import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class PetInfo {

    private String name;

    private Integer age;

    @Enumerated(STRING)
    private PetType type;

    @Enumerated(STRING)
    @Column(nullable = false)
    private PetSex sex;

    private String bio;

    //== validation constructor ==//
    @Builder
    private PetInfo(
            final String name,
            final Integer age,
            final PetType type,
            final PetSex sex,
            final String bio
    ) {
        validateName(name);
        validateAge(age);
        validateType(type);
        validateBio(bio);
        this.name = name;
        this.age = age;
        this.type = type;
        this.sex = sex;
        this.bio = bio;
    }

    public static PetInfo of(
            final String name,
            final Integer age,
            final PetType type,
            final PetSex sex,
            final String bio
    ) {
        return PetInfo.builder()
                .name(name)
                .age(age)
                .type(type)
                .sex(sex)
                .bio(bio)
                .build();
    }

    //== validation Logic ==//
    private void validateType(final PetType type) {
        if (isNull(type)) {
            throw new BusinessException(PET_TYPE_NULL);
        } else if (type.name().equals(ERROR.name())) {
            throw new BusinessException(PET_TYPE_INVALID);
        }
    }

    private void validateName(final String name) {
        if (isNull(name)) {
            throw new BusinessException(PET_NAME_NULL);
        } else if (name.length() > 20) {
            throw new BusinessException(PET_NAME_TOO_LONG);
        } else if (!name.matches("^[가-힣a-zA-Z]+$")) {
            throw new BusinessException(PET_NAME_INVALID_CHARACTERS);
        }
    }

    private void validateAge(final Integer age) {
        if (age < 1 || age > 100) {
            throw new BusinessException(PET_AGE_INVALID);
        }
    }

    private void validateBio(final String bio) {
        if (!isNull(bio) && bio.length() > 300) {
            throw new BusinessException(PET_BIO_TOO_LONG);
        }
    }
}
