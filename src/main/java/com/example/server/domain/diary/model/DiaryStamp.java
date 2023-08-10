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

import lombok.Getter;

@Entity
@Getter
@Table(name = "diary_stamp_tb")
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

	public void updateDiary(Diary diary) {
		this.diary = diary;
	}
}
