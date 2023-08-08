package com.example.server.domain.pet.api.dto;

import com.example.server.domain.image.model.Image;
import com.example.server.domain.pet.model.PetInfo;
import com.example.server.domain.pet.model.PetTempProtectedInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static lombok.AccessLevel.PROTECTED;

@Builder
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class PetProfileResponse {

    Long petId;
    String providerId;
    Image profileImage;
    PetInfo petInfo;
    PetTempProtectedInfo petTempProtectedInfo;
    Boolean isSuccess;
    LocalDateTime timestamp;

    //== static factory method ==//
    public static PetProfileResponse of(
            Long petId,
            String providerId,
            Image profileImage,
            PetInfo petInfo,
            PetTempProtectedInfo petTempProtectedInfo
    ) {
        return PetProfileResponse.builder()
                .petId(petId)
                .providerId(providerId)
                .profileImage(profileImage)
                .petInfo(petInfo)
                .petTempProtectedInfo(petTempProtectedInfo)
                .isSuccess(true)
                .timestamp(now())
                .build();
    }
}
