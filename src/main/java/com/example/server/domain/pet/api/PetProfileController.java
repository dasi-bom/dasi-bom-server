package com.example.server.domain.pet.api;

import static com.example.server.domain.pet.api.dto.PetProfileResponse.*;
import static org.springframework.http.MediaType.*;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.domain.image.model.Image;
import com.example.server.domain.pet.api.dto.PetProfileCreateRequest;
import com.example.server.domain.pet.api.dto.PetProfileResponse;
import com.example.server.domain.pet.application.PetService;
import com.example.server.domain.pet.model.Pet;
import com.example.server.global.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
public class PetProfileController {

	private final PetService petService;

	@PostMapping("/profile")
	public ResponseEntity<PetProfileResponse> createPetProfile(
		@RequestBody PetProfileCreateRequest reqDto,
		@AuthenticationPrincipal UserDetails userDetails
	) {
		Pet pet = petService.createProfile(reqDto, userDetails.getUsername());
		return ApiResponse.created(of(
			pet.getId(),
			userDetails.getUsername(),
			pet.getPetInfo(),
			pet.getPetTempProtectedInfo()));
	}

	@PostMapping(value = "/profile/images", consumes = MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Image> uploadPetProfileImage(
		@RequestParam MultipartFile multipartFile,
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestParam Long petId
	) throws IOException {
		Pet pet = petService.uploadProfileImage(
			userDetails.getUsername(),
			petId,
			multipartFile);
		return ApiResponse.success(pet.getProfile());
	}

}
