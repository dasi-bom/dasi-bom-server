package com.example.server.domain.pet.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class PetProfileUpdateRequest {

	private String type;

	private String name;

	private Integer age;

	private String sex;

	private String bio;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startTempProtectedDate;
}
