package com.example.server.domain.pet.application;

import com.example.server.domain.member.application.MemberFindService;
import com.example.server.domain.member.model.Member;
import com.example.server.domain.pet.api.dto.PetProfileCreateRequest;
import com.example.server.domain.pet.model.Pet;
import com.example.server.domain.pet.model.PetInfo;
import com.example.server.domain.pet.model.PetTempProtectedInfo;
import com.example.server.domain.pet.model.constants.PetSex;
import com.example.server.domain.pet.model.constants.PetTempProtectedStatus;
import com.example.server.domain.pet.model.constants.PetType;
import com.example.server.domain.pet.persistence.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.example.server.domain.pet.model.constants.PetTempProtectedStatus.*;
import static java.time.LocalDate.now;
import static java.util.Objects.isNull;

@Service
@Transactional
@RequiredArgsConstructor
public class PetService {

    private final MemberFindService memberFindService;
    private final PetRepository petRepository;

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
        Member owner = memberFindService.findMemberByProviderId(username);

        PetType type = PetType.toEnum(req.getType());
        PetSex sex = PetSex.toEnum(req.getSex());

        PetInfo petInfo = PetInfo.of(req.getName(), req.getAge(), type, sex, req.getBio());
        PetTempProtectedInfo petProtectedInfo =
                PetTempProtectedInfo.of(IN_PROGRESS, req.getStartTempProtectedDate());
        return petRepository.save(Pet.of(owner, petInfo, petProtectedInfo));
    }
}
