package com.example.server.domain.diary.api;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.domain.diary.api.assembler.DiaryResponseAssembler;
import com.example.server.domain.diary.api.dto.DiaryResponse;
import com.example.server.domain.diary.api.dto.DiarySaveRequest;
import com.example.server.domain.diary.application.DiaryService;
import com.example.server.domain.diary.model.Diary;
import com.example.server.global.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

	private final DiaryService diaryService;
	private final DiaryResponseAssembler diaryResponseAssembler;

	// @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	// public ResponseEntity<DiaryResponse> createDiary(
	// 	@AuthenticationPrincipal UserDetails userDetails,
	// 	@RequestPart @Valid DiarySaveRequest diarySaveRequest,
	// 	@RequestPart(required = false) List<MultipartFile> multipartFiles
	// ) throws IOException {
	// 	Diary diary = diaryService.createDiary(userDetails.getUsername(), diarySaveRequest, multipartFiles);
	// 	DiaryResponse response = diaryResponseAssembler.toResponse(diary);
	// 	return ApiResponse.created(response);
	// }

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<DiaryResponse> createDiary(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestParam("petId") Long petId,
		@RequestParam("category") String category,
		@RequestParam("content") String content,
		@RequestParam("stamps") String stamps,
		@RequestParam("isPublic") Boolean isPublic,
		@RequestPart(required = false) List<MultipartFile> multipartFiles
	) throws IOException {
		DiarySaveRequest diarySaveRequest = new DiarySaveRequest();
		diarySaveRequest.setPetId(petId);
		diarySaveRequest.setCategory(category);
		diarySaveRequest.setContent(content);
		diarySaveRequest.setStamps(Arrays.asList(stamps.split(", "))); // 스탬프를 리스트로 변환
		diarySaveRequest.setIsPublic(isPublic);

		Diary diary = diaryService.createDiary(userDetails.getUsername(), diarySaveRequest, multipartFiles);
		DiaryResponse response = diaryResponseAssembler.toResponse(diary);
		return ApiResponse.created(response);
	}
}
