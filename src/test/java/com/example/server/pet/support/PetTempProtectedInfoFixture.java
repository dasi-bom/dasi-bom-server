package com.example.server.pet.support;

import com.example.server.domain.pet.model.PetTempProtectedInfo;
import com.example.server.domain.pet.model.constants.PetTempProtectedStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static com.example.server.domain.pet.model.constants.PetTempProtectedStatus.IN_PROGRESS;
import static java.time.LocalDate.of;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public enum PetTempProtectedInfoFixture {

    VALID_PET(IN_PROGRESS, of(2023, 1, 1));

    private PetTempProtectedStatus status;
    private LocalDate startTempProtectedDate;

    public PetTempProtectedInfo toEntity() {
        return PetTempProtectedInfo.of(
                status,
                startTempProtectedDate
        );
    }
}
