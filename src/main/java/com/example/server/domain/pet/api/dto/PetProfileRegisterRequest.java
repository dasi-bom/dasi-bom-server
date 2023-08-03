package com.example.server.domain.pet.api.dto;

import static lombok.AccessLevel.*;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.example.server.domain.pet.model.constants.Sex;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class PetProfileRegisterRequest {
	private String type;
	private MultipartFile multipartFile;
	private String name;
	private Integer age;
	private Sex sex;
	private String bio;
	private LocalDateTime startTempProtectedDate;
}
