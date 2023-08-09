package com.example.server.domain.pet.api;

import com.example.server.domain.pet.api.dto.PetProfileCreateRequest;
import com.example.server.domain.pet.api.dto.PetProfileResponse;
import com.example.server.domain.pet.application.PetService;
import com.example.server.domain.pet.model.Pet;
import com.example.server.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.server.domain.pet.api.dto.PetProfileResponse.of;

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
                pet.getProfileImage(),
                pet.getPetInfo(),
                pet.getPetTempProtectedInfo()));
    }
}
