package com.example.server.domain.pet.model;

import com.example.server.domain.pet.model.constants.PetTempProtectedStatus;
import com.example.server.global.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import java.time.LocalDate;

import static com.example.server.global.exception.ErrorCode.*;
import static java.util.Objects.isNull;
import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class PetTempProtectedInfo {

    @Enumerated(STRING)
    private PetTempProtectedStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startTempProtectedDate;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endTempProtectedDate;

    @Builder
    private PetTempProtectedInfo(
            final PetTempProtectedStatus status,
            final LocalDate startTempProtectedDate
    ) {
        validateStartTempProtectedDate(startTempProtectedDate);
        this.status = status;
        this.startTempProtectedDate = startTempProtectedDate;
    }

    public static PetTempProtectedInfo of(
            final PetTempProtectedStatus status,
            final LocalDate startTempProtectedDate
    ) {
        return PetTempProtectedInfo
                .builder()
                .status(status)
                .startTempProtectedDate(startTempProtectedDate)
                .build();
    }

    private void validateStartTempProtectedDate(final LocalDate tempProtectedDate) {
        if (isNull(tempProtectedDate)) {
            throw new BusinessException(PET_TEMP_PROTECTED_SRT_DATE_INVALID);
        } else if (tempProtectedDate.isBefore(
                //todo temp) 2020년 1월 1일 이전 임시보호일 등록에 대해 Exception
                LocalDate.of(2020, 1, 1))) {
            throw new BusinessException(PET_TEMP_PROTECTED_SRT_DATE_TOO_EARLY);
        } else if (tempProtectedDate.isAfter(LocalDate.now())) {
            throw new BusinessException(PET_TEMP_PROTECTED_SRT_DATE_IN_FUTURE);
        }
    }
}