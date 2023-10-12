package com.example.server.domain.challenge.api.dto;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeSaveRequest {

	private String name;
	private String description;
	private LocalDate startDate;
	private LocalDate endDate;
	private String challengeType;
}
