package com.example.server.domain.pet.model;

import static com.example.server.domain.pet.model.constants.PetType.*;
import static java.util.Objects.*;
import static javax.persistence.EnumType.*;
import static lombok.AccessLevel.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;

import com.example.server.domain.pet.model.constants.PetSex;
import com.example.server.domain.pet.model.constants.PetType;
import com.example.server.global.exception.BusinessException;
import com.example.server.global.exception.errorcode.PetErrorCode;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class PetInfo {

	private String name;

	private Integer age;

	@Enumerated(STRING)
	private PetType type;

	@Enumerated(STRING)
	private PetSex sex;

	@Column(length = 300)
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

	//== validation Logic ==//
	private void validateType(final PetType type) {
		if (isNull(type)) {
			throw new BusinessException(PetErrorCode.PET_TYPE_NULL);
		} else if (type.name().equals(ERROR.name())) {
			throw new BusinessException(PetErrorCode.PET_TYPE_INVALID);
		}
	}

	private void validateName(final String name) {
		if (isNull(name)) {
			throw new BusinessException(PetErrorCode.PET_NAME_NULL);
		} else if (name.length() > 20) {
			throw new BusinessException(PetErrorCode.PET_NAME_TOO_LONG);
		} else if (!name.matches("^[가-힣a-zA-Z]+$")) {
			throw new BusinessException(PetErrorCode.PET_NAME_INVALID_CHARACTERS);
		}
	}

	private void validateAge(final Integer age) {
		if (age < 1 || age > 100) {
			throw new BusinessException(PetErrorCode.PET_AGE_INVALID);
		}
	}

	private void validateBio(final String bio) {
		if (!isNull(bio) && bio.length() > 300) {
			throw new BusinessException(PetErrorCode.PET_BIO_TOO_LONG);
		}
	}
}
