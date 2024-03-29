package com.example.server.domain.diary.api;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.domain.diary.api.assembler.DiaryResponseAssembler;
import com.example.server.domain.diary.api.dto.DiaryBriefResponse;
import com.example.server.domain.diary.api.dto.DiaryIdResponse;
import com.example.server.domain.diary.api.dto.DiaryResponse;
import com.example.server.domain.diary.api.dto.DiarySaveRequest;
import com.example.server.domain.diary.application.DiaryService;
import com.example.server.domain.diary.model.Diary;
import com.example.server.domain.diary.model.condition.ReadCondition;
import com.example.server.global.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

	private final DiaryService diaryService;
	private final DiaryResponseAssembler diaryResponseAssembler;

	/**
	 * 다음 DIARY ID 발급 (동물 일기 저장 시 필요)
	 */
	@GetMapping("/issue-id")
	public ResponseEntity<DiaryIdResponse> issueId() {
		return ApiResponse.success(diaryService.issueId());
	}

	// 일기 작성
	@PostMapping("/{diary-id}")
	public ResponseEntity<DiaryResponse> createDiary(
		@PathVariable("diary-id") Long diaryId,
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestBody @Valid DiarySaveRequest diarySaveRequest
	) {
		Diary diary = diaryService.createDiary(diaryId, userDetails.getUsername(), diarySaveRequest);
		DiaryResponse response = diaryResponseAssembler.toResponse(diary);
		return ApiResponse.created(response);
	}

	// 이미지 업로드
	@PostMapping(value = "/img/{diary-id}",
		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<DiaryResponse> uploadImage(
		@PathVariable("diary-id") Long diaryId,
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestPart List<MultipartFile> multipartFiles
	) throws IOException {
		DiaryResponse response = diaryService.uploadImage(diaryId, userDetails.getUsername(), multipartFiles);
		return ApiResponse.created(response);
	}

	// 일기 수정
	@PutMapping("/{diary-id}")
	public ResponseEntity<DiaryResponse> updateDiary(
		@PathVariable("diary-id") Long diaryId,
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestBody @Valid DiarySaveRequest diarySaveRequest
	) {
		Diary diary = diaryService.updateDiary(diaryId, userDetails.getUsername(), diarySaveRequest);
		DiaryResponse response = diaryResponseAssembler.toResponse(diary);
		return ApiResponse.success(response);
	}

	// 일기 삭제
	@DeleteMapping("/{diary-id}")
	public ResponseEntity<String> deleteDiary(
		@PathVariable("diary-id") Long diaryId,
		@AuthenticationPrincipal UserDetails userDetails
	) {
		diaryService.deleteDiary(diaryId, userDetails.getUsername());
		return ApiResponse.noContent("성공적으로 삭제되었습니다.");
	}

	// 일기 목록을 조회할 수 있는 엔드포인트
	@GetMapping("/list")
	public Slice<DiaryBriefResponse> getAll(
		Long cursor,
		@PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageRequest
	) {
		return diaryService.getAll(cursor, new ReadCondition(), pageRequest);
	}
}
