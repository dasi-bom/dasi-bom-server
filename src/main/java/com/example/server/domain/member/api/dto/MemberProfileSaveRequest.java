package com.example.server.domain.member.api.dto;

import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfileSaveRequest {

	@NotBlank(message = "닉네임은 필수 입력 값입니다.")
	private String nickname;

	@Builder
	public MemberProfileSaveRequest(String nickname) {
		this.nickname = nickname;
	}
}
