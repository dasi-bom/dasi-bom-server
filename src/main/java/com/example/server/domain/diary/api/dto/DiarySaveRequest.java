package com.example.server.domain.diary.api.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiarySaveRequest {

	@NotNull(message = "폴더는 필수 입력 값입니다.")
	private Long folderId;

	private Long challengeId;

	@NotNull(message = "내용은 필수 입력 값입니다.")
	@Size(max = 1000, message = "본문은 최대 1000자까지 입력할 수 있습니다.")
	private String content;

	@NotNull(message = "스탬프는 필수 입력 값입니다.")
	private List<Long> stamps = new ArrayList<>();
	// private List<String> stamps = new ArrayList<>();

	@NotNull(message = "공개 범위는 필수 선택 값입니다.")
	private Boolean isPublic;
}
