package com.example.server.domain.pet.application;

import static com.example.server.domain.pet.model.Pet.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.member.model.Member;
import com.example.server.domain.pet.model.Pet;
import com.example.server.domain.pet.api.dto.PetProfileRequest;
import com.example.server.domain.pet.persistence.PetRepository;
import com.example.server.domain.member.application.MemberFindService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PetService {

	private final PetRepository petRepository;
	private final MemberFindService memberFindService;

	@Transactional
	// todo 한 사용자가 중복된 이름의 반려동물을 생성하는 경우?
	public void createProfile(PetProfileRequest req, Long memberId) {
		Member owner = memberFindService.findByUserId(memberId);
		Pet pet = of(owner, req.getType(), req.getProfileImage(), req.getName(), req.getAge());
		// todo 프로필 사진 S3 등록 로직 추가 예정
		petRepository.save(pet);
	}
}
