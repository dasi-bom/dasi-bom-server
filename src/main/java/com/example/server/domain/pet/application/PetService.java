package com.example.server.domain.pet.application;

import static com.example.server.domain.pet.model.Pet.*;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.image.model.Image;
import com.example.server.domain.member.application.MemberFindService;
import com.example.server.domain.member.application.MemberService;
import com.example.server.domain.member.model.Member;
import com.example.server.domain.pet.api.dto.PetProfileRegisterRequest;
import com.example.server.domain.pet.model.Pet;
import com.example.server.domain.pet.persistence.PetRepository;
import com.example.server.global.util.S3Uploader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PetService {

	private static final String DIR_PATH = "Profile/Pet";

	private final PetRepository petRepository;
	private final MemberService memberService;
	private final S3Uploader s3Uploader;
	private final MemberFindService memberFindService;

	public Pet createProfile(PetProfileRegisterRequest req, String username) throws IOException {
		Member owner = memberFindService.findBySecurityUsername(username);
		Image profileImage = s3Uploader.uploadSingleImage(req.getMultipartFile(), DIR_PATH);
		return petRepository.save(
			of(owner,
				req.getType(),
				profileImage,
				req.getName(),
				req.getStartTempProtectedDate(),
				req.getAge(),
				req.getBio()));
	}
}
