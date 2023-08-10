package com.example.server.domain.diary.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.domain.diary.api.dto.DiarySaveRequest;
import com.example.server.domain.diary.application.DiaryService;
import com.example.server.global.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

	private final DiaryService diaryService;

	@PostMapping()
	public ResponseEntity<Void> createDiary(@AuthenticationPrincipal UserDetails userDetails,
		@RequestPart @Valid DiarySaveRequest diarySaveRequest,
		@RequestPart(required = false) List<MultipartFile> multipartFiles) {
		diaryService.createDiary(userDetails.getUsername(), diarySaveRequest, multipartFiles);
		return ApiResponse.success(null);
	}
}
