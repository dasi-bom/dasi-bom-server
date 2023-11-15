package com.example.server.domain.pet.application;

import static com.example.server.domain.pet.model.constants.PetTempProtectedStatus.*;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.domain.member.model.Member;
import com.example.server.domain.member.persistence.MemberRepository;
import com.example.server.domain.pet.api.dto.PetIdResponse;
import com.example.server.domain.pet.api.dto.PetProfileCreateRequest;
import com.example.server.domain.pet.model.Pet;
import com.example.server.domain.pet.model.PetInfo;
import com.example.server.domain.pet.model.PetTempProtectedInfo;
import com.example.server.domain.pet.model.constants.PetSex;
import com.example.server.domain.pet.model.constants.PetType;
import com.example.server.domain.pet.persistence.PetRepository;
import com.example.server.global.exception.BusinessException;
import com.example.server.global.exception.errorcode.ImageErrorCode;
import com.example.server.global.exception.errorcode.MemberErrorCode;
import com.example.server.global.exception.errorcode.PetErrorCode;
import com.example.server.global.util.S3Uploader;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PetService {

	private static final String DIR_NAME = "Profile/Pet";
	private final MemberRepository memberRepository;
	private final PetRepository petRepository;
	private final S3Uploader s3Uploader;

	/**
	 * public - createProfile
	 *
	 * @param req      : requestBody
	 * @param username : spring security username(PK)
	 * @return : savedPet
	 */
	public Pet createProfile(
		Long petId,
		PetProfileCreateRequest req,
		String username
	) {
		Member owner = memberRepository.findByProviderId(username)
			.orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
		Optional<Pet> oPet = petRepository.findById(petId);
		Pet pet;

		PetType type = PetType.toEnum(req.getType());
		PetSex sex = PetSex.toEnum(req.getSex());

		PetInfo petInfo = PetInfo.builder()
			.name(req.getName())
			.age(req.getAge())
			.type(type)
			.sex(sex)
			.bio(req.getBio())
			.build();

		PetTempProtectedInfo petProtectedInfo = PetTempProtectedInfo.builder()
			.status(IN_PROGRESS)
			.startTempProtectedDate(req.getStartTempProtectedDate())
			.build();

		if (oPet.isPresent()) { // pet이 이미 존재하는 경우 - 사용자가 이미지 등록을 먼저 호출한 경우
			pet = oPet.get();
			pet.updateOwner(owner);
			pet.updatePetInfo(petInfo);
			pet.updatePetTempProtectedInfo(petProtectedInfo);
		} else { // 신규 생성
			pet = Pet.builder()
				.id(petId)
				.owner(owner)
				.petInfo(petInfo)
				.petTempProtectedInfo(petProtectedInfo)
				.build();
			petRepository.save(pet);
		}

		return pet;
	}

	public Pet uploadProfileImage(
		String username,
		Long petId,
		MultipartFile multipartFile
	) throws IOException {
		if (multipartFile == null) {
			throw new BusinessException(ImageErrorCode.FILE_NOT_EXIST_ERROR);
		}
		Optional<Pet> oPet = petRepository.findById(petId);
		Pet pet;
		if (oPet.isPresent()) { // 이미 펫이 존재하면 펫 프로필 이미지 업데이트
			pet = oPet.get();
			String foundUsername = pet.getOwner().getUsername();
			if (!foundUsername.equals(username)) {
				throw new BusinessException(PetErrorCode.PET_OWNER_INVALID);
			}
			pet.updateProfileImage(s3Uploader.uploadSingleImage(multipartFile, DIR_NAME));
		} else { // 펫이 존재하지 않으면 신규 생성
			pet = Pet.builder()
				.id(petId)
				.build();
			pet.updateProfileImage(s3Uploader.uploadSingleImage(multipartFile, DIR_NAME));
			petRepository.save(pet);
		}
		return pet;
	}

	public PetIdResponse issueId() {
		Optional<Long> lastId = petRepository.getLastId();
		return PetIdResponse.builder()
			.petId(lastId.isEmpty() ? 1L : lastId.get() + 1L)
			.build();
	}
}
