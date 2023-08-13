package com.example.server.domain.diary.api;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.domain.diary.api.dto.DiaryResponse;
import com.example.server.domain.diary.api.dto.DiarySaveRequest;
import com.example.server.domain.diary.application.DiaryService;
import com.example.server.domain.diary.model.Diary;
import com.example.server.domain.image.model.Image;
import com.example.server.global.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

	private final DiaryService diaryService;

	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<DiaryResponse> createDiary(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestPart @Valid DiarySaveRequest diarySaveRequest,
		@RequestPart(required = false) List<MultipartFile> multipartFiles
	) throws IOException {
		Diary diary = diaryService.createDiary(userDetails.getUsername(), diarySaveRequest, multipartFiles, "Diary");
		return ApiResponse.created(DiaryResponse.of(
			diary.getPet().getPetInfo().getName(),
			diary.getCategory(),
			diary.getChallengeTopic(),
			(diary.getImages() != null)
				? diary.getImages().stream().map(Image::getImgUrl).collect(Collectors.toList()) : null,
			diary.getAuthor().getNickname(),
			diary.getContent(),
			diary.getDiaryStamps()
				.stream().map(diaryStamp -> diaryStamp.getStamp().getStampType().name()).collect(Collectors.toList()),
			diary.getIsPublic()
		));
	}
}
