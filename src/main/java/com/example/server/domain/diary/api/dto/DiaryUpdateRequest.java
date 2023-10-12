package com.example.server.domain.diary.api.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiaryUpdateRequest {

	private Long petId;

	private Long challengeId;

	// private String category;

	@Size(max = 1000, message = "본문은 최대 1000자까지 입력할 수 있습니다.")
	private String content;

	private List<String> stamps = new ArrayList<>();

	private Boolean isPublic;
}
