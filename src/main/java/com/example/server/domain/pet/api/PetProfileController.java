// package com.example.server.domain.pet.api;
//
// import static com.example.server.domain.pet.api.dto.PetProfileResponse.*;
// import static java.time.LocalDateTime.*;
// import static org.springframework.http.MediaType.*;
//
// import java.io.IOException;
//
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.multipart.MultipartFile;
//
// import com.example.server.domain.image.model.Image;
// import com.example.server.domain.pet.api.dto.PetIdResponse;
// import com.example.server.domain.pet.api.dto.PetProfileCreateRequest;
// import com.example.server.domain.pet.api.dto.PetProfileResponse;
// import com.example.server.domain.pet.application.PetService;
// import com.example.server.domain.pet.model.Pet;
// import com.example.server.global.dto.ApiResponse;
//
// import lombok.RequiredArgsConstructor;
//
// @RestController
// @RequestMapping("/pet")
// @RequiredArgsConstructor
// public class PetProfileController {
//
// 	private final PetService petService;
//
// 	@PostMapping("/profile/{pet-id}")
// 	public ResponseEntity<PetProfileResponse> createPetProfile(
// 		@PathVariable("pet-id") Long petId,
// 		@RequestBody PetProfileCreateRequest reqDto,
// 		@AuthenticationPrincipal UserDetails userDetails
// 	) {
// 		Pet pet = petService.createProfile(petId, reqDto, userDetails.getUsername());
// 		PetProfileResponse response = builder()
// 			.petId(pet.getId())
// 			.providerId(userDetails.getUsername())
// 			.petInfo(pet.getPetInfo())
// 			.petTempProtectedInfo(pet.getPetTempProtectedInfo())
// 			.isSuccess(true)
// 			.timestamp(now())
// 			.build();
// 		return ApiResponse.created(response);
// 	}
//
// 	// [동물 신규 생성 시 프로필 이미지 등록] 또는 [프로필 이미지 업데이트] 모두에 활용
// 	@PostMapping(value = "/profile/images/{pet-id}", consumes = MULTIPART_FORM_DATA_VALUE)
// 	public ResponseEntity<Image> uploadPetProfileImage(
// 		@PathVariable("pet-id") Long petId,
// 		@RequestParam MultipartFile multipartFile,
// 		@AuthenticationPrincipal UserDetails userDetails
// 	) throws IOException {
// 		Pet pet = petService.uploadProfileImage(
// 			userDetails.getUsername(),
// 			petId,
// 			multipartFile);
// 		return ApiResponse.success(pet.getProfile());
// 	}
//
// 	/**
// 	 * 다음 PET ID 발급 (동물 프로필 이미지 저장 시 필요)
// 	 */
// 	@GetMapping("/issue-id")
// 	public ResponseEntity<PetIdResponse> issueId() {
// 		return ApiResponse.success(petService.issueId());
// 	}
//
// }
