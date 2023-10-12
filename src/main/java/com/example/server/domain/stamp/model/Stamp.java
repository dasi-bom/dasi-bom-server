package com.example.server.domain.stamp.model;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.server.domain.diary.model.DiaryStamp;
import com.example.server.domain.member.model.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "stamp_tb")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Stamp {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Column(unique = true)
	private String name;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "admin_id")
	private Member admin; // 등록한 사람
	// private Admin admin;

	@OneToMany(mappedBy = "stamp", cascade = CascadeType.ALL)
	private List<DiaryStamp> diaries = new ArrayList<>();

	@Builder
	private Stamp(
		final String name,
		final Member admin
	) {
		this.admin = admin;
		this.name = name;
	}

}
