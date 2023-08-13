package com.example.server.domain.pet.model;

import static com.example.server.global.exception.ErrorCode.*;
import static java.util.Objects.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.server.domain.image.model.Image;
import com.example.server.domain.member.model.Member;
import com.example.server.global.auditing.BaseEntity;
import com.example.server.global.exception.BusinessException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@Table(name = "pet_tb")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Pet extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "owner_id")
	private Member owner;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "image_id")
	private Image profile;

	@Embedded
	private PetInfo petInfo;

	@Embedded
	private PetTempProtectedInfo petTempProtectedInfo;

	//== validation constructor ==//
	@Builder
	private Pet(
		final Member owner,
		final PetInfo petInfo,
		final PetTempProtectedInfo petTempProtectedInfo
	) {
		validateOwner(owner);
		this.owner = owner;
		this.petInfo = petInfo;
		this.petTempProtectedInfo = petTempProtectedInfo;
	}

	//== static factory method ==//
	public static Pet of(
		final Member owner,
		final PetInfo petInfo,
		final PetTempProtectedInfo petTempProtectedInfo
	) {
		Pet pet = Pet.builder()
			.petInfo(petInfo)
			.petTempProtectedInfo(petTempProtectedInfo)
			.build();
		pet.addOwner(owner);
		return pet;
	}

	//== validation method ==//
	private void validateOwner(final Member owner) {
		if (isNull(owner)) {
			throw new BusinessException(PET_OWNER_NULL);
		}
	}

	//== utility method ==//
	private void addOwner(Member owner) {
		this.owner = owner;
		owner.addPet(this);
	}
}
