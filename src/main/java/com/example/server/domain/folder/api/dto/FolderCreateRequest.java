package com.example.server.domain.folder.api.dto;

import static lombok.AccessLevel.*;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class FolderCreateRequest {
	@NotNull
	private String name;
}
