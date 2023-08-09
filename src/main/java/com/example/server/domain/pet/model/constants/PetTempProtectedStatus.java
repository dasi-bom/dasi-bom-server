package com.example.server.domain.pet.model.constants;

import com.example.server.global.exception.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static com.example.server.global.exception.ErrorCode.PET_TEMP_PROTECTED_SRT_DATE_INVALID;

@Getter
@RequiredArgsConstructor
public enum PetTempProtectedStatus {
    IN_PROGRESS("임시보호 진행중"),
    DONE("임시보호 완료"),
    ERROR("임시보호 상태 에러");

    private final String message;

    public static PetTempProtectedStatus toEnum(String status) {
        PetTempProtectedStatus enumStatus = Arrays.stream(PetTempProtectedStatus.values())
                .filter(value -> value.name().equals(status))
                .findFirst()
                .orElse(ERROR);
        if (enumStatus.equals(ERROR)) {
            throw new BusinessException(PET_TEMP_PROTECTED_SRT_DATE_INVALID);
        }
        return enumStatus;
    }
}
