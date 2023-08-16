package com.example.server.domain.member.api;

import static com.example.server.global.exception.ErrorCode.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.domain.member.api.dto.FirstProfileCreateResponse;
import com.example.server.domain.member.api.dto.MemberProfileResponse;
import com.example.server.domain.member.api.dto.MemberProfileSaveRequest;
import com.example.server.domain.member.api.dto.MemberProfileSaveResponse;
import com.example.server.domain.member.application.MemberFindService;
import com.example.server.domain.member.application.MemberService;
import com.example.server.domain.member.model.Member;
import com.example.server.domain.pet.api.dto.PetProfileResponse;
import com.example.server.domain.pet.application.PetFindService;
import com.example.server.domain.pet.model.Pet;
import com.example.server.global.dto.ApiResponse;
import com.example.server.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final MemberFindService memberFindService;
	private final PetFindService petFindService;

	@PatchMapping("/profile")
	public ResponseEntity<MemberProfileSaveResponse> updateProfile(@RequestBody MemberProfileSaveRequest reqDto,
		@AuthenticationPrincipal UserDetails userDetails) {
		Member member = memberService.updateProfile(reqDto, userDetails.getUsername());
		return ApiResponse.success(MemberProfileSaveResponse.of(
			member.getName(),
			member.getUsername(),
			member.getEmail(),
			member.getMobile(),
			member.getProvider(),
			member.getProviderId(),
			(member.getProfileImage() != null) ? member.getProfileImage().getImgUrl() : null,
			member.getNickname()
		));
	}

	@PostMapping(value = "/profile/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<MemberProfileSaveResponse> uploadProfileImage(@RequestParam MultipartFile multipartFile,
		@AuthenticationPrincipal UserDetails userDetails) throws IOException {
		if (multipartFile == null) {
			throw new BusinessException(FILE_NOT_EXIST_ERROR);
		}
		Member member = memberService.uploadProfileImage(userDetails.getUsername(), multipartFile);
		return ApiResponse.success(MemberProfileSaveResponse.of(
			member.getName(),
			member.getUsername(),
			member.getEmail(),
			member.getMobile(),
			member.getProvider(),
			member.getProviderId(),
			(member.getProfileImage() != null) ? member.getProfileImage().getImgUrl() : null,
			member.getNickname()
		));
	}

	// 최초 프로필 등록 완료 (동물 프로필까지 등록했을 때에만 사용)
	@GetMapping("/profile/{pet-id}")
	public ResponseEntity<FirstProfileCreateResponse> firstCreateProfile(
		@PathVariable("pet-id") Long petId,
		@AuthenticationPrincipal UserDetails userDetails
	) {
		Member member = memberFindService.findMemberByProviderId(userDetails.getUsername());
		if (!petFindService.findPetsByOwner(member).contains(petId)) {
			throw new BusinessException(PET_NOT_FOUND);
		}
		Pet pet = petFindService.findPetById(petId);

		return ApiResponse.success(FirstProfileCreateResponse.of(
			member.getNickname(),
			(member.getProfileImage() != null) ? member.getProfileImage().getImgUrl() : null,
			pet.getPetInfo().getName(),
			(pet.getProfile() != null) ? pet.getProfile().getImgUrl() : null
		));
	}

	// 사용자 프로필 조회 엔드포인트
	@GetMapping("/profile")
	public ResponseEntity<MemberProfileResponse> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
		Member member = memberFindService.findMemberByProviderId(userDetails.getUsername());
		List<PetProfileResponse> petProfileResponses = member.getPets().stream()
			.map(pet -> PetProfileResponse.of(pet.getId(), member.getProviderId(), pet.getPetInfo(),
				pet.getPetTempProtectedInfo()))
			.collect(Collectors.toList());

		return ApiResponse.success(MemberProfileResponse.of(
			member.getName(),
			member.getUsername(),
			member.getEmail(),
			member.getMobile(),
			member.getProvider(),
			member.getProviderId(),
			(member.getProfileImage() != null) ? member.getProfileImage().getImgUrl() : null,
			member.getNickname(),
			petProfileResponses
		));
	}
}
