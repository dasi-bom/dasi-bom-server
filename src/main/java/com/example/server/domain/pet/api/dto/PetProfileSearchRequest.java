package com.example.server.domain.pet.api.dto;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class PetProfileSearchRequest {

	private Long petId;
}
