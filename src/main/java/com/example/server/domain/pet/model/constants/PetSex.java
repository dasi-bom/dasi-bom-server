package com.example.server.domain.pet.model.constants;

import com.example.server.global.exception.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static com.example.server.global.exception.ErrorCode.PET_SEX_INVALID;

@Getter
@RequiredArgsConstructor
public enum PetSex {
    MALE("수컷"),
    FEMALE("암컷"),
    ERROR("동물 성별 에러");

    public static PetSex toEnum(String sex) {
        PetSex petSex = Arrays.stream(PetSex.values())
                .filter(value -> value.name().equals(sex.toUpperCase()))
                .findFirst()
                .orElse(ERROR);
        if (petSex.equals(ERROR)) {
            throw new BusinessException(PET_SEX_INVALID);
        }
        return petSex;
    }

    private final String message;
}
