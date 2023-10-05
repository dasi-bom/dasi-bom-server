package com.example.server.domain.diary.api.dto;

import static lombok.AccessLevel.*;

import java.time.LocalDateTime;

import com.example.server.domain.diary.model.Diary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 일기 리스트 조회에 사용되는 dto
 */
@Builder
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class DiaryBriefResponse {

	private Long id;
	private String thumbnail;
	private String content;
	private LocalDateTime createdDate;

	public static DiaryBriefResponse from(Diary diary) {
		return DiaryBriefResponse.builder()
			.id(diary.getId())
			.thumbnail(diary.getImages().size() == 0 ? null : diary.getImages().get(0).getImgUrl())
			.content(diary.getContent())
			.createdDate(diary.getCreatedDate())
			.build();
	}
}
