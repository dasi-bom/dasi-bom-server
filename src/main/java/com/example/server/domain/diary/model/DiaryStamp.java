package com.example.server.domain.diary.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.server.domain.stamp.model.Stamp;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "diary_stamp_tb")
@NoArgsConstructor
public class DiaryStamp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "diary_stamp_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "diary_id")
	private Diary diary;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "stamp_id")
	private Stamp stamp;

	@Builder
	private DiaryStamp(
		final Diary diary,
		final Stamp stamp
	) {
		this.diary = diary;
		this.stamp = stamp;
	}

	public static DiaryStamp of(
		final Stamp stamp
	) {
		return DiaryStamp.builder()
			.stamp(stamp)
			.build();
	}

	public void updateDiary(Diary diary) {
		this.diary = diary;
	}

	// diaryId 와 stampId 의 매핑 제거
	public void removeDiaryStamp() {
		this.diary = null;
		this.stamp = null;
	}
}
