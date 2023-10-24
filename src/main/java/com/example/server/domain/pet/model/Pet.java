package com.example.server.domain.pet.model;

import static java.util.Objects.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.server.domain.diary.model.Diary;
import com.example.server.domain.image.model.Image;
import com.example.server.domain.member.model.Member;
import com.example.server.global.auditing.BaseEntity;
import com.example.server.global.exception.BusinessException;
import com.example.server.global.exception.errorcode.PetErrorCode;

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

	@OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
	private List<Diary> diaries = new ArrayList<>();

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

	//== validation method ==//
	private void validateOwner(final Member owner) {
		if (isNull(owner)) {
			throw new BusinessException(PetErrorCode.PET_OWNER_NULL);
		}
	}

	//== utility method ==//
	public void updateDiaries(Diary diary) {
		this.diaries.add(diary);
	}

	public void updateProfileImage(Image image) {
		this.profile = image;
	}
}
