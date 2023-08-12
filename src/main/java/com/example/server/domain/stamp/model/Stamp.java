package com.example.server.domain.stamp.model;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.server.domain.diary.model.DiaryStamp;
import com.example.server.domain.stamp.model.constants.StampType;

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

	@Enumerated(EnumType.STRING)
	private StampType stampType;

	@OneToMany(mappedBy = "stamp", cascade = CascadeType.ALL)
	private List<DiaryStamp> diaries = new ArrayList<>();

	@Builder
	private Stamp(final StampType stampType) {
		this.stampType = stampType;
	}

	public static Stamp of(final StampType stampType) {
		return Stamp.builder()
			.stampType(stampType)
			.build();
	}
}
