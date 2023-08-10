package com.example.server.domain.member.model.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
	ROLE_USER("사용자"),
	ROLE_ADMIN("관리자");

	private final String message;

	@Override
	public String toString() {
		return message;
	}
}
