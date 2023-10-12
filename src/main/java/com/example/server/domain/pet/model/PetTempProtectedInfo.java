package com.example.server.domain.pet.model;

import static com.example.server.global.exception.ErrorCode.*;
import static java.util.Objects.*;
import static javax.persistence.EnumType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;

import com.example.server.domain.pet.model.constants.PetTempProtectedStatus;
import com.example.server.global.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class PetTempProtectedInfo {

	@Enumerated(STRING)
	private PetTempProtectedStatus status;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private LocalDate startTempProtectedDate;

	@Column
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

	private void validateStartTempProtectedDate(final LocalDate tempProtectedDate) {
		if (isNull(tempProtectedDate)) {
			throw new BusinessException(PET_TEMP_PROTECTED_SRT_DATE_INVALID);
		} else if (tempProtectedDate.isBefore(
			LocalDate.of(2020, 1, 1))) {
			throw new BusinessException(PET_TEMP_PROTECTED_SRT_DATE_TOO_EARLY);
		} else if (tempProtectedDate.isAfter(LocalDate.now())) {
			throw new BusinessException(PET_TEMP_PROTECTED_SRT_DATE_IN_FUTURE);
		}
	}
}
