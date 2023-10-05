package com.example.server.domain.diary.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.example.server.domain.diary.api.dto.DiaryBriefResponse;
import com.example.server.domain.diary.model.condition.ReadCondition;

public interface CustomDiaryRepository {
	Slice<DiaryBriefResponse> getDiaryBriefScroll(Long cursorId, ReadCondition condition, Pageable pageable);
}
