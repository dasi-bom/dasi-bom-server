package com.example.server.domain.diary.api.dto;

import static lombok.AccessLevel.*;

import java.util.List;

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
	private String folder;
	private Boolean isChallenge; // false 이면 일상 일기 (챌린지 X)
	private String challenge; // 일상 일기라면 null
	private List<String> images;
	private String author;
	private String content;
	private List<String> diaryStamps;
	private Boolean isPublic;
}
