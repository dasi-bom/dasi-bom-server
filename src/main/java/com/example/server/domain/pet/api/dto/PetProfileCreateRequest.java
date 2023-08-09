package com.example.server.domain.pet.api.dto;

import static lombok.AccessLevel.*;

import java.time.LocalDate;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class PetProfileCreateRequest {

	private String name;
	private Integer age;
	private String type;
	private String sex;
	private String bio;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startTempProtectedDate;
}
