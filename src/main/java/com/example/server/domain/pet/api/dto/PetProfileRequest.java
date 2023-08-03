package com.example.server.domain.pet.api.dto;

import static lombok.AccessLevel.*;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class PetProfileRequest {

	private String type;

	private String name;

	private Integer age;

	private String sex;

	private String bio;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startTempProtectedDate;
}
