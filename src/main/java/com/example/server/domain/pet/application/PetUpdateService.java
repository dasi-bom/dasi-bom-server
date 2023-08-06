package com.example.server.domain.pet.application;

import static com.example.server.global.exception.ErrorCode.*;

import java.util.List;

import com.example.server.domain.pet.api.dto.PetProfileUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.member.application.MemberFindService;
import com.example.server.domain.member.model.Member;
import com.example.server.domain.pet.model.Pet;
import com.example.server.domain.pet.persistence.PetRepository;
import com.example.server.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PetUpdateService {

	private final MemberFindService memberFindService;
	private final PetFindService petFindService;

	public Pet updatePetProfile(
		PetProfileUpdateRequest reqDto,
		String username,
		Long petId
	) {
		Member owner = memberFindService.findMemberByProviderId(username);
		Pet pet = petFindService.findByPetId(petId);
		List<Long> petIds = petFindService.findPetsByOwner(owner);

		if (!petIds.contains(petId)) {
			throw new BusinessException(PET_OWNER_INVALID);
		}
		pet.updateProfile(reqDto);
		return pet;
	}
}
