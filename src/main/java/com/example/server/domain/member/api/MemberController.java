package com.example.server.domain.member.api;

import static com.example.server.global.exception.ErrorCode.*;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.domain.member.api.assembler.MemberProfileResponseAssembler;
import com.example.server.domain.member.api.assembler.MemberProfileSaveResponseAssembler;
import com.example.server.domain.member.api.dto.MemberProfileResponse;
import com.example.server.domain.member.api.dto.MemberProfileSaveRequest;
import com.example.server.domain.member.api.dto.MemberProfileSaveResponse;
import com.example.server.domain.member.application.MemberService;
import com.example.server.domain.member.model.Member;
import com.example.server.domain.member.persistence.MemberQueryRepository;
import com.example.server.global.dto.ApiResponse;
import com.example.server.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final MemberQueryRepository memberQueryRepository;
	private final MemberProfileSaveResponseAssembler memberProfileSaveResponseAssembler;
	private final MemberProfileResponseAssembler memberProfileResponseAssembler;

	@PatchMapping("/profile")
	public ResponseEntity<MemberProfileSaveResponse> updateProfile(@RequestBody MemberProfileSaveRequest reqDto,
		@AuthenticationPrincipal UserDetails userDetails) {
		Member member = memberService.updateProfile(reqDto, userDetails.getUsername());
		return ApiResponse.success(memberProfileSaveResponseAssembler.toResponse(member));
	}

	@PostMapping(value = "/profile/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<MemberProfileSaveResponse> uploadProfileImage(@RequestParam MultipartFile multipartFile,
		@AuthenticationPrincipal UserDetails userDetails) throws IOException {
		if (multipartFile == null) {
			throw new BusinessException(FILE_NOT_EXIST_ERROR);
		}
		Member member = memberService.uploadProfileImage(userDetails.getUsername(), multipartFile);
		return ApiResponse.success(memberProfileSaveResponseAssembler.toResponse(member));
	}

	@GetMapping("/profile")
	public ResponseEntity<MemberProfileResponse> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
		Member member = memberQueryRepository.findByProviderId(userDetails.getUsername())
			.orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
		return ApiResponse.success(memberProfileResponseAssembler.toResponse(member));
	}
}
