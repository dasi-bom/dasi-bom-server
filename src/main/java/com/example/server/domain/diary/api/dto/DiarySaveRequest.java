package com.example.server.domain.diary.api.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiarySaveRequest {

	@NotNull(message = "카테고리는 필수 선택 값입니다.")
	private String category;

	private String challengeTopic;

	private String content;

	private List<String> stamps = new ArrayList<>();

	@NotNull(message = "공개 여부는 필수 선택 값입니다.")
	private Boolean isPublic;
}
