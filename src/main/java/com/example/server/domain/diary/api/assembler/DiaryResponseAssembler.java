package com.example.server.domain.diary.api.assembler;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.server.domain.challenge.model.Challenge;
import com.example.server.domain.diary.api.dto.DiaryResponse;
import com.example.server.domain.diary.model.Diary;
import com.example.server.domain.diary.model.DiaryStamp;
import com.example.server.domain.image.model.Image;

@Component
public class DiaryResponseAssembler {
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
				.map(diaryStamp -> diaryStamp.getStamp().getName())
				.collect(Collectors.toList())
			)
			.orElse(null);
	}

	public DiaryResponse toResponse(Diary diary) {
		return DiaryResponse.builder()
			.id(diary.getId())
			.pet(diary.getPet().getPetInfo().getName())
			.isChallenge(diary.getIsChallenge())
			.challenge(getChallengeName(diary.getChallenge()))
			.images(getImages(diary.getImages()))
			.author(diary.getAuthor().getNickname())
			.content(diary.getContent())
			.diaryStamps(getStamps(diary.getDiaryStamps()))
			.isPublic(diary.getIsPublic())
			.build();
	}

	private String getChallengeName(Challenge challenge) {
		return (challenge == null) ? null : challenge.getName();
	}
}
