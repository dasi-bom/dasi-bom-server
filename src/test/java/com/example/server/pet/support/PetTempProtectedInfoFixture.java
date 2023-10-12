package com.example.server.pet.support;

import static com.example.server.domain.pet.model.constants.PetTempProtectedStatus.*;

import java.time.LocalDate;

import com.example.server.domain.pet.model.PetTempProtectedInfo;
import com.example.server.domain.pet.model.constants.PetTempProtectedStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public enum PetTempProtectedInfoFixture {

	VALID_PET(IN_PROGRESS, LocalDate.of(2023, 1, 1));

	private PetTempProtectedStatus status;
	private LocalDate startTempProtectedDate;

	public PetTempProtectedInfo toEntity() {
		return PetTempProtectedInfo.builder()
			.status(status)
			.startTempProtectedDate(startTempProtectedDate)
			.build();
	}
}
