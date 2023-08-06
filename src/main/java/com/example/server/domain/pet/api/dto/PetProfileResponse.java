package com.example.server.domain.pet.api.dto;

import com.example.server.domain.pet.model.constants.PetTempProtectedStatus;
import com.example.server.domain.pet.model.constants.PetType;
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
    String petName;
    PetType petType;
    PetTempProtectedStatus status;
    Boolean isSuccess;
    LocalDateTime timestamp;

    //== static factory method ==//
    public static PetProfileResponse of(
            Long petId,
            String petName,
            PetType petType,
            PetTempProtectedStatus status
    ) {
        return PetProfileResponse.builder()
                .petId(petId)
                .petName(petName)
                .petType(petType)
                .isSuccess(true)
                .status(status)
                .timestamp(now())
                .build();
    }
}
