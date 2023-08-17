package com.example.server.domain.member.api;

import static com.example.server.global.exception.ErrorCode.*;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.domain.member.api.dto.MemberProfileSaveRequest;
import com.example.server.domain.member.application.MemberService;
import com.example.server.global.dto.ApiResponse;
import com.example.server.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PatchMapping("/profile")
	public ResponseEntity<Void> updateProfile(@RequestBody MemberProfileSaveRequest reqDto,
		@AuthenticationPrincipal UserDetails userDetails) {
		memberService.updateProfile(reqDto, userDetails.getUsername());
		return ApiResponse.success(null);
	}

	@PostMapping(value = "/profile/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> uploadProfileImage(@RequestParam MultipartFile multipartFile,
		@AuthenticationPrincipal UserDetails userDetails) throws IOException {
		if (multipartFile == null) {
			throw new BusinessException(FILE_NOT_EXIST_ERROR);
		}
		memberService.uploadProfileImage(userDetails.getUsername(), multipartFile);

		return ApiResponse.success(null);
	}

}
