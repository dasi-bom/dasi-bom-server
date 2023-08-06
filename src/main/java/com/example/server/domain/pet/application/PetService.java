package com.example.server.domain.pet.application;

import static com.example.server.domain.pet.model.Pet.of;
import static com.example.server.global.exception.ErrorCode.*;

import java.io.IOException;
import java.util.List;

import com.example.server.domain.pet.model.constants.PetType;
import com.example.server.domain.pet.persistence.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.domain.member.application.MemberFindService;
import com.example.server.domain.member.model.Member;
import com.example.server.domain.pet.api.dto.PetProfileCreateRequest;
import com.example.server.domain.pet.model.Pet;
import com.example.server.domain.pet.model.constants.Sex;
import com.example.server.global.exception.BusinessException;
import com.example.server.global.util.S3Uploader;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PetService {

	private static final String DIR_PATH = "/Profile/Pet";

	private final S3Uploader s3Uploader;
	private final MemberFindService memberFindService;
	private final PetFindService petFindService;
	private final PetRepository petRepository;

	/**
	 * public - createProfile
	 * @param req               : requestBody
	 * @param username          : spring security username(PK)
	 * @return                  : savedPet
	 */
	public Pet createProfile(
		PetProfileCreateRequest req,
		String username
	) {
		Member owner = memberFindService.findMemberByProviderId(username);
		return petRepository.save(
			of(owner,
					PetType.valueOf(req.getType()),
				req.getName(),
				Sex.valueOf(req.getSex()),
				req.getStartTempProtectedDate(),
				req.getAge(),
				req.getBio()));
	}

	/**
	 * public - uploadProfileImage
	 * @param username                    : spring security username -> owner PK
	 * @param petId                       : petId -> pet PK
	 * @param multipartFile               : img files
	 * @throws IOException                : when thrown S3 Exception
	 */
	public void uploadProfileImage(
		String username,
		Long petId,
		MultipartFile multipartFile
	) throws IOException {
		Member owner = memberFindService.findMemberByProviderId(username);
		Pet pet = petFindService.findByPetId(petId);
		List<Long> petIds = petFindService.findPetsByOwner(owner);

		if (!petIds.contains(petId)) {
			throw new BusinessException(PET_OWNER_INVALID);
		}

		pet.updateProfileImage(s3Uploader.uploadSingleImage(multipartFile, DIR_PATH));
	}
}
