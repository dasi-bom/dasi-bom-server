package com.example.server.domain.challenge.model;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.server.domain.member.model.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "challenge_tb")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
public class Challenge {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	@Column(unique = true)
	private String name;
	private String description;
	private String rewards;
	private String howToParticipate;
	private String precautions;
	private LocalDate startDate;
	private LocalDate endDate;

	@Enumerated(EnumType.STRING)
	private ChallengeType challengeType;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "admin_id")
	private Member admin; // 등록한 사람
	// private Admin admin;

}
