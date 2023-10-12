package com.example.server.domain.diary.model;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.server.domain.challenge.model.Challenge;
import com.example.server.domain.diary.api.dto.DiaryUpdateRequest;
import com.example.server.domain.image.model.Image;
import com.example.server.domain.member.model.Member;
import com.example.server.domain.pet.model.Pet;
import com.example.server.global.auditing.BaseEntity;
import com.nimbusds.oauth2.sdk.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "diary_tb")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
// @Builder
public class Diary extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "pet_id")
	private Pet pet;

	private Boolean isChallenge; // false 이면 일상 일기 (챌린지 X)

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "challenge_id")
	private Challenge challenge; // 일상 일기라면 null

	@OneToMany(cascade = CascadeType.ALL)
	private List<Image> images = new ArrayList<>();

	@ManyToOne(fetch = LAZY)
	private Member author;

	@Column(length = 1200)
	private String content;

	private LocalDateTime deletedDate;

	private Boolean isDeleted;

	@OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
	private List<DiaryStamp> diaryStamps = new ArrayList<>();

	private Boolean isPublic;

	@Builder
	private Diary(
		final Pet pet,
		final Challenge challenge,
		final List<Image> images,
		final Member author,
		final String content,
		final List<DiaryStamp> diaryStamps,
		final Boolean isPublic
	) {
		this.pet = pet;
		this.isChallenge = validateIsChallenge(challenge);
		this.challenge = challenge;
		this.images = validateAndInitializeImages(images);
		this.author = author;
		this.content = content;
		this.isDeleted = false;
		this.diaryStamps = validateAndInitializeDiaryStamps(diaryStamps);
		this.isPublic = isPublic;
	}

	private static List<Image> validateAndInitializeImages(List<Image> images) {
		return (images != null) ? images : new ArrayList<>();
	}

	private static List<DiaryStamp> validateAndInitializeDiaryStamps(List<DiaryStamp> diaryStamps) {
		return (diaryStamps != null) ? diaryStamps : new ArrayList<>();
	}

	private static Boolean validateIsChallenge(Challenge challenge) {
		return (challenge == null) ? false : true;
	}

	public void addImages(List<Image> images) {
		this.images.addAll(images);
	}

	private void addDiaryStamps(DiaryStamp diaryStamp) {
		diaryStamps.add(diaryStamp);
		diaryStamp.updateDiary(this);
	}

	public void updatePet(Pet pet) {
		this.pet = pet;
	}

	public void updateDiary(DiaryUpdateRequest diaryUpdateRequest, List<DiaryStamp> diaryStamps) {
		if (StringUtils.isNotBlank(diaryUpdateRequest.getContent())) {
			this.content = diaryUpdateRequest.getContent();
		}
		if (diaryStamps != null) {
			this.diaryStamps.clear();
			for (DiaryStamp ds : diaryStamps) {
				addDiaryStamps(ds);
			}
		}
		if (diaryUpdateRequest.getIsPublic() != null) {
			this.isPublic = diaryUpdateRequest.getIsPublic();
		}
	}

	public void updateChallenge(Challenge challenge) {
		this.isChallenge = true;
		this.challenge = challenge;
	}

	public void deleteDiary() {
		this.isDeleted = Boolean.TRUE;
		this.deletedDate = LocalDateTime.now();
	}

}
