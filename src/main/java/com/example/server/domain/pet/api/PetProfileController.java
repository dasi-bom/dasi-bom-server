package com.example.server.domain.pet.api;

import static com.example.server.domain.pet.api.dto.PetProfileRegisterResponse.*;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.domain.pet.api.dto.PetProfileRegisterRequest;
import com.example.server.domain.pet.api.dto.PetProfileRegisterResponse;
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
	public ResponseEntity<PetProfileRegisterResponse> createPetProfile(
		@RequestBody PetProfileRegisterRequest reqDto,
		@AuthenticationPrincipal UserDetails userDetails
	) throws IOException {
		Pet pet = petService.createProfile(reqDto, userDetails.getUsername());
		return ApiResponse.success(of(pet.getId(), pet.getName()));
	}
}
