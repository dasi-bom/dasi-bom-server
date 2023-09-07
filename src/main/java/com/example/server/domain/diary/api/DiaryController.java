package com.example.server.domain.diary.api;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.domain.diary.api.assembler.DiaryResponseAssembler;
import com.example.server.domain.diary.api.dto.DiaryResponse;
import com.example.server.domain.diary.api.dto.DiarySaveRequest;
import com.example.server.domain.diary.api.dto.DiaryUpdateRequest;
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

	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<DiaryResponse> createDiaryExceptForImage(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestBody @Valid DiarySaveRequest diarySaveRequest
	) {
		Diary diary = diaryService.createDiaryExceptForImage(userDetails.getUsername(), diarySaveRequest);
		DiaryResponse response = diaryResponseAssembler.toResponse(diary);
		return ApiResponse.created(response);
	}

	@PostMapping(value = "/img/{diary-id}", consumes = {MediaType.APPLICATION_JSON_VALUE,
		MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<DiaryResponse> uploadImage(
		@PathVariable("diary-id") Long diaryId,
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestPart List<MultipartFile> multipartFiles
	) throws IOException {
		Diary diary = diaryService.uploadImage(diaryId, userDetails.getUsername(), multipartFiles);
		DiaryResponse response = diaryResponseAssembler.toResponse(diary);
		return ApiResponse.created(response);
	}

	@PatchMapping("/{diary-id}")
	public ResponseEntity<DiaryResponse> updateDiary(
		@PathVariable("diary-id") Long diaryId,
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestPart @Valid DiaryUpdateRequest diaryUpdateRequest,
		@RequestPart(required = false) List<MultipartFile> multipartFiles
	) throws IOException {
		diaryService.updateDiary(diaryId, userDetails.getUsername(), diaryUpdateRequest, multipartFiles);
		return ApiResponse.success(null); // todo: response 생성
	}
}
