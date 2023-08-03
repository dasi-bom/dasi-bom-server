package com.example.server.domain.pet.application;

import static com.example.server.global.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.member.model.Member;
import com.example.server.domain.pet.model.Pet;
import com.example.server.domain.pet.persistence.PetQueryRepository;
import com.example.server.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetFindService {

	private final PetQueryRepository petQueryRepository;

	public Pet findByPetId(Long petId) {
		return petQueryRepository.findById(petId)
			.orElseThrow(() -> new BusinessException(PET_NOT_FOUND));
	}

	public List<Long> findPetsByOwner(Member owner) {
		List<Long> petIdsByOwnerId = petQueryRepository.findPetIdsByOwnerId(owner);
		if (petIdsByOwnerId.isEmpty()) {
			throw new BusinessException(PET_NOT_FOUND);
		}
		return petIdsByOwnerId;
	}
}
