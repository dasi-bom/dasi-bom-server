package com.example.server.domain.folder.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.domain.diary.api.dto.DiaryResponse;
import com.example.server.domain.folder.api.dto.FolderCreateRequest;
import com.example.server.domain.folder.api.dto.FolderResponse;
import com.example.server.domain.folder.application.FolderService;
import com.example.server.global.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/folder")
@RequiredArgsConstructor
public class FolderController {
	private final FolderService folderService;

	// 폴더 생성
	@PostMapping()
	public ResponseEntity<FolderResponse> createFolder(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestBody @Valid FolderCreateRequest folderCreateRequest
	) {
		FolderResponse response = folderService.createFolder(userDetails.getUsername(), folderCreateRequest);
		return ApiResponse.created(response);
	}

	// 폴더에 있는 일기 조회
	@GetMapping("/{folder-id}")
	public ResponseEntity<List<DiaryResponse>> fetchFolder(
		@PathVariable("folder-id") Long folderId,
		@AuthenticationPrincipal UserDetails userDetails
	) {
		List<DiaryResponse> response = folderService.fetchFolder(userDetails.getUsername(), folderId);
		return ApiResponse.success(response);
	}
}
