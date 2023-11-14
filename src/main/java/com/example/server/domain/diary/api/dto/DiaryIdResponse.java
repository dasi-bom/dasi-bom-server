package com.example.server.domain.diary.api.dto;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class DiaryIdResponse {
	Long diaryId;
}
