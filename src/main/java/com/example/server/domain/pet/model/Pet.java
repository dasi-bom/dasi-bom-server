package com.example.server.domain.pet.model;

import static com.example.server.global.exception.ErrorCode.*;
import static com.fasterxml.jackson.annotation.JsonFormat.Shape.*;
import static java.time.LocalDateTime.*;
import static java.util.Objects.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.example.server.domain.image.model.Image;
import com.example.server.domain.member.model.Member;
import com.example.server.domain.pet.model.constants.Sex;
import com.example.server.global.auditing.BaseEntity;
import com.example.server.global.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Table(name = "pet_tb")
@NoArgsConstructor(access = PROTECTED)
public class Pet extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "owner_id")
	private Member owner;

	private String type;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "image_id")
	private Image profileImage;

	private String name;

	private Integer age;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Sex sex;

	private String bio;

	@JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
	private LocalDateTime startTempProtectedDate;

	@JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
	private LocalDateTime endTempProtectedDate;

	//== validation constructor ==//
	@Builder
	private Pet(
		final Member owner,
		final String type,
		final Image profileImage,
		final String name,
		final LocalDateTime startTempProtectedDate,
		final Integer age,
		final String bio
	) {
		validateOwner(owner);
		validateType(type);
		validateName(name);
		validateStartTempProtectedDate(startTempProtectedDate);
		validateAge(age);
		validateBio(bio);
		this.owner = owner;
		this.type = type;
		this.profileImage = profileImage;
		this.name = name;
		this.startTempProtectedDate = startTempProtectedDate;
		this.age = age;
		this.bio = bio;
	}

	//== static factory method ==//
	public static Pet of(
		final Member owner,
		final String type,
		final Image profileImage,
		final String name,
		final LocalDateTime startTempProtectedDate,
		final Integer age,
		final String bio
	) {
		return Pet.builder()
			.owner(owner)
			.type(type)
			.profileImage(profileImage)
			.name(name)
			.startTempProtectedDate(startTempProtectedDate)
			.age(age)
			.bio(bio)
			.build();
	}

	//== validation method ==//
	private void validateOwner(final Member owner) {
		if (isNull(owner)) {
			throw new BusinessException(PET_OWNER_NULL);
		}
	}

	private void validateType(final String type) {
		if (isNull(type)) {
			throw new BusinessException(PET_TYPE_NULL);
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

	private void validateStartTempProtectedDate(final LocalDateTime tempProtectedDate) {
		if (tempProtectedDate.isBefore(
			LocalDateTime.of(1900, 1, 1, 0, 0)) ||
			tempProtectedDate.isAfter(now())) {
			throw new BusinessException(PET_PROTECTED_DATE_INVALID);
		}
	}
}
