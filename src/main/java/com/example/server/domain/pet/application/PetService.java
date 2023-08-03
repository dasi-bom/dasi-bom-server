package com.example.server.domain.pet.application;

import static com.example.server.domain.pet.model.Pet.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.member.application.MemberFindService;
import com.example.server.domain.member.model.Member;
import com.example.server.domain.pet.api.dto.PetProfileRegisterRequest;
import com.example.server.domain.pet.model.Pet;
import com.example.server.domain.pet.model.constants.Sex;
import com.example.server.domain.pet.persistence.PetRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PetService {

	private final PetRepository petRepository;
	private final MemberFindService memberFindService;

	public Pet createProfile(PetProfileRegisterRequest req, String username) {
		Member owner = memberFindService.findBySecurityUsername(username);
		return petRepository.save(
			of(owner,
				req.getType(),
				req.getName(),
				Sex.valueOf(req.getSex()),
				req.getStartTempProtectedDate(),
				req.getAge(),
				req.getBio()));
	}
}
