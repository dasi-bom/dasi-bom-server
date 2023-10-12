package com.example.server.domain.stamp.api.dto;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StampSaveRequest {

	@NotNull(message = "스탬프 이름은 필수 값입니다.")
	private String name;
}
