package com.example.server.domain.diary.api.assembler;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.server.domain.diary.api.dto.DiaryResponse;
import com.example.server.domain.diary.model.Diary;
import com.example.server.domain.diary.model.DiaryStamp;
import com.example.server.domain.image.model.Image;

@Component
public class DiaryResponseAssembler {
	public DiaryResponse toResponse(Diary diary) {
		return DiaryResponse.builder()
			.pet(diary.getPet().getPetInfo().getName())
			.category(diary.getCategory())
			.images(getImages(diary.getImages()))
			.author(diary.getAuthor().getNickname())
			.content(diary.getContent())
			.diaryStamps(getStamps(diary.getDiaryStamps()))
			.isPublic(diary.getIsPublic())
			.build();
	}

	private static List<String> getImages(List<Image> images) {
		return Optional.ofNullable(images)
			.map(imgs -> imgs.stream()
				.map(Image::getImgUrl)
				.collect(Collectors.toList())
			)
			.orElse(null);
	}

	private static List<String> getStamps(List<DiaryStamp> diaryStamps) {
		return Optional.ofNullable(diaryStamps)
			.map(stamps -> stamps.stream()
				.map(diaryStamp -> diaryStamp.getStamp().getStampType().name())
				.collect(Collectors.toList())
			)
			.orElse(null);
	}
}