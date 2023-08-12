package com.example.server.domain.diary.model;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.server.domain.diary.model.constants.Category;
import com.example.server.domain.diary.model.constants.ChallengeTopic;
import com.example.server.domain.image.model.Image;
import com.example.server.domain.member.model.Member;
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

	// private Pet pet;

	@Enumerated(EnumType.STRING)
	private Category category;

	@Enumerated(EnumType.STRING)
	private ChallengeTopic challengeTopic;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Image> images = new ArrayList<>();

	@ManyToOne
	private Member author;

	private String content;

	private LocalDateTime deletedDate;

	private Boolean isDeleted;

	@OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
	private List<DiaryStamp> diaryStamps = new ArrayList<>();

	private Boolean isPublic;

	@Builder
	private Diary(
		final Category category,
		final ChallengeTopic challengeTopic,
		final List<Image> images,
		final Member author,
		final String content,
		final List<DiaryStamp> diaryStamps,
		final Boolean isPublic
	) {
		this.category = category;
		this.challengeTopic = challengeTopic;
		this.images = images;
		this.author = author;
		this.content = content;
		this.diaryStamps = (diaryStamps != null) ? diaryStamps : new ArrayList<>();
		this.isPublic = isPublic;
	}

	private void addDiaryStamps(DiaryStamp diaryStamp) {
		diaryStamps.add(diaryStamp);
		diaryStamp.updateDiary(this);
	}

	public static Diary of(
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
		diaryStamps.forEach(diary::addDiaryStamps);

		return diary;
	}
}
