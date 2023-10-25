package com.example.server.domain.challenge.api.dto;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 스탬프 조회에 사용되는 dto
 */
@Builder
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ChallengeResponse {

	private String name;
	private String challengeType;
	private String registeredBy;
}
