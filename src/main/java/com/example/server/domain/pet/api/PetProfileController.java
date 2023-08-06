package com.example.server.domain.pet.api;

import static com.example.server.domain.pet.api.dto.PetProfileResponse.*;
import static com.example.server.global.exception.ErrorCode.*;
import static org.springframework.http.MediaType.*;

import java.io.IOException;

import com.example.server.domain.pet.api.dto.PetProfileUpdateRequest;
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

import com.example.server.domain.pet.api.dto.PetProfileCreateRequest;
import com.example.server.domain.pet.api.dto.PetProfileResponse;
import com.example.server.domain.pet.application.PetService;
import com.example.server.domain.pet.application.PetUpdateService;
import com.example.server.domain.pet.model.Pet;
import com.example.server.global.dto.ApiResponse;
import com.example.server.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
public class PetProfileController {

	private final PetService petService;
	private final PetUpdateService petUpdateService;

	@PostMapping("/profile")
	public ResponseEntity<PetProfileResponse> createPetProfile(
		@RequestBody PetProfileCreateRequest reqDto,
		@AuthenticationPrincipal UserDetails userDetails
	) {
		Pet pet = petService.createProfile(reqDto, userDetails.getUsername());
		return ApiResponse.success(of(pet.getId(), pet.getName()));
	}

	@PostMapping(value = "/profile/images/", consumes = MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> uploadProfileImage(
		@RequestParam MultipartFile multipartFile,
		@RequestParam Long petId,
		@AuthenticationPrincipal UserDetails userDetails
	) throws IOException {
		if (multipartFile == null) {
			throw new BusinessException(FILE_NOT_EXIST_ERROR);
		}
		petService.uploadProfileImage(userDetails.getUsername(), petId, multipartFile);
		return ApiResponse.created(null);
	}

	@PatchMapping("/profile")
	public ResponseEntity<PetProfileResponse> updatePetProfile(
		@RequestParam Long petId,
		@RequestBody PetProfileUpdateRequest reqDto,
		@AuthenticationPrincipal UserDetails userDetails
	) {
		Pet pet = petUpdateService.updatePetProfile(reqDto, userDetails.getUsername(), petId);
		return ApiResponse.success(PetProfileResponse.of(pet.getId(), pet.getName()));
	}
}
