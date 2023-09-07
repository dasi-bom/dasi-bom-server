package com.example.server.domain.diary.api.dto;

import static lombok.AccessLevel.*;

import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.example.server.domain.diary.model.constants.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 일기 세부 조회에 사용되는 dto
 */
@Builder
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class DiaryResponse {

	private Long id;
	private String pet;
	@Enumerated(EnumType.STRING)
	private Category category;
	private List<String> images;
	private String author;
	private String content;
	private List<String> diaryStamps;
	private Boolean isPublic;
}
