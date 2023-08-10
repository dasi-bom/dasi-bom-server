package com.example.server.domain.diary.api.dto;

import java.util.List;

import com.example.server.domain.diary.model.DiaryStamp;
import com.example.server.domain.image.model.Image;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiarySaveRequest {

	private String category;

	private String challengeTopic;

	private List<Image> images;

	private String content;

	private List<DiaryStamp> diaryStamps;

	private Boolean isPublic;
}
