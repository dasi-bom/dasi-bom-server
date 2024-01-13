package com.example.server.domain.folder.api.dto;

import static lombok.AccessLevel.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class FolderUpdateRequest {
	private String name;
}
