package com.example.server.domain.diary.model.condition;

import lombok.Getter;

@Getter
public class ReadCondition {

	private Boolean isDeleted;

	// 전체 일기 조회
	public ReadCondition() {
		this.isDeleted = false;
	}

}
