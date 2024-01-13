package com.example.server.domain.challenge.api.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeSaveRequest {
	@NotNull(message = "챌린지 이름은 필수 값입니다.")
	private String name;
	private String description;
	private String rewards;
	private String howToParticipate;
	private String precautions;
	private LocalDate startDate;
	private LocalDate endDate;
	@NotNull(message = "챌린지 타입은 필수 값입니다.")
	private String challengeType;
}
