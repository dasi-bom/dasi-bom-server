package com.example.server.domain.pet.application;

import com.example.server.domain.member.application.MemberFindService;
import com.example.server.domain.member.model.Member;
import com.example.server.domain.pet.api.dto.PetProfileCreateRequest;
import com.example.server.domain.pet.model.Pet;
import com.example.server.domain.pet.model.PetInfo;
import com.example.server.domain.pet.model.PetTempProtectedInfo;
import com.example.server.domain.pet.model.constants.PetSex;
import com.example.server.domain.pet.model.constants.PetType;
import com.example.server.domain.pet.persistence.PetRepository;
import com.example.server.global.exception.BusinessException;
import com.example.server.global.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.example.server.domain.pet.model.constants.PetTempProtectedStatus.IN_PROGRESS;
import static com.example.server.global.exception.ErrorCode.FILE_NOT_EXIST_ERROR;
import static com.example.server.global.exception.ErrorCode.PET_OWNER_INVALID;
import static java.util.Objects.isNull;

@Service
@Transactional
@RequiredArgsConstructor
public class PetService {

    private static final String DIR_NAME = "Profile/Pet";

    private final MemberFindService memberFindService;
    private final PetFindService petFindService;
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
            PetProfileCreateRequest req,
            String username
    ) {
        Member owner = memberFindService.findMemberByUsername(username);

        PetType type = PetType.toEnum(req.getType());
        PetSex sex = PetSex.toEnum(req.getSex());

        PetInfo petInfo = PetInfo.of(req.getName(), req.getAge(), type, sex, req.getBio());
        PetTempProtectedInfo petProtectedInfo =
                PetTempProtectedInfo.of(IN_PROGRESS, req.getStartTempProtectedDate());
        return petRepository.save(Pet.of(owner, petInfo, petProtectedInfo));
    }

    public Pet uploadProfileImage(
            String username,
            Long petId,
            MultipartFile multipartFile
    ) throws IOException {
        if (isNull(multipartFile)) {
            throw new BusinessException(FILE_NOT_EXIST_ERROR);
        }
        Pet pet = petFindService.findPetById(petId);
        String foundUsername = pet.getOwner().getUsername();
        if (!foundUsername.equals(username)) {
            throw new BusinessException(PET_OWNER_INVALID);
        }
        pet.updateProfileImage(s3Uploader.uploadSingleImage(multipartFile, DIR_NAME));
        return pet;
    }
}
