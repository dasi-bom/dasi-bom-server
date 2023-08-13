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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.server.domain.diary.model.constants.Category;
import com.example.server.domain.diary.model.constants.ChallengeTopic;
import com.example.server.domain.image.model.Image;
import com.example.server.domain.member.model.Member;
import com.example.server.domain.pet.model.Pet;
import com.example.server.global.auditing.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "diary_tb")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Diary extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "pet_id")
	private Pet pet;

	@Enumerated(EnumType.STRING)
	private Category category;

	@Enumerated(EnumType.STRING)
	private ChallengeTopic challengeTopic;

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
		final Category category,
		final ChallengeTopic challengeTopic,
		final List<Image> images,
		final Member author,
		final String content,
		final List<DiaryStamp> diaryStamps,
		final Boolean isPublic
	) {
		this.pet = pet;
		this.category = category;
		this.challengeTopic = challengeTopic;
		this.images = validateAndInitializeImages(images);
		this.author = author;
		this.content = content;
		this.diaryStamps = validateAndInitializeDiaryStamps(diaryStamps);
		this.isPublic = isPublic;
	}

	private List<Image> validateAndInitializeImages(List<Image> images) {
		return (images != null) ? images : new ArrayList<>();
	}

	private List<DiaryStamp> validateAndInitializeDiaryStamps(List<DiaryStamp> diaryStamps) {
		return (diaryStamps != null) ? diaryStamps : new ArrayList<>();
	}

	public static Diary of(
		final Pet pet,
		final Category category,
		final ChallengeTopic challengeTopic,
		final Member author,
		final String content,
		final List<DiaryStamp> diaryStamps,
		final Boolean isPublic
	) {
		Diary diary = Diary.builder()
			.category(category)
			.challengeTopic(challengeTopic)
			.author(author)
			.content(content)
			.isPublic(isPublic)
			.build();
		diary.addPet(pet);
		diaryStamps.forEach(diary::addDiaryStamps);

		return diary;
	}

	public void addImages(List<Image> images) {
		this.images.addAll(images);
	}

	private void addDiaryStamps(DiaryStamp diaryStamp) {
		diaryStamps.add(diaryStamp);
		diaryStamp.updateDiary(this);
	}

	private void addPet(Pet pet) {
		this.pet = pet;
		pet.updateDiaries(this);
	}
}
