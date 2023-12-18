package com.example.server.domain.folder.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
