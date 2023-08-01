package com.example.server.domain.pet.api.dto;

import static lombok.AccessLevel.*;

import com.example.server.domain.image.model.Image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class PetProfileRequest {
	private String type;
	private Image profileImage;
	private String name;
	private Integer age;
}
