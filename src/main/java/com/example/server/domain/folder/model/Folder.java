package com.example.server.domain.folder.model;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.server.domain.diary.model.Diary;
import com.example.server.domain.member.model.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(name = "folder_tb")
public class Folder {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "folder_id")
	private Long id;
	private String name;
	private Integer cnt;
	private LocalDateTime deletedDate;
	private Boolean isDeleted;
	@OneToMany
	private List<Diary> diaryList = new ArrayList<>();
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "owner_id")
	private Member owner;

	public void addDiaryList(Diary diary) {
		diaryList.add(diary);
	}

	public void updateFolderName(String name) {
		this.name = name;
	}

	public void addCnt() {
		if (cnt == null) {
			cnt = 0;
		}
		this.cnt += 1;
	}

	public void deleteFolder() {
		this.isDeleted = true;
		this.deletedDate = LocalDateTime.now();
	}
}
