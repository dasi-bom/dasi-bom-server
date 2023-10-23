package com.example.server.domain.diary.persistence;

import static com.example.server.domain.diary.model.QDiary.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.example.server.domain.diary.api.dto.DiaryBriefResponse;
import com.example.server.domain.diary.model.Diary;
import com.example.server.domain.diary.model.condition.ReadCondition;
import com.example.server.global.util.SliceUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomDiaryRepositoryImpl implements CustomDiaryRepository {

	private final JPAQueryFactory queryFactory;

	// 일기 조회
	@Override
	public Slice<DiaryBriefResponse> getDiaryBriefScroll(Long cursorId, ReadCondition condition, Pageable pageable) {
		List<Diary> diaryList = queryFactory
			.select(diary)
			.from(diary)
			.where(
				eqIsDeleted(condition.getIsDeleted()), // 삭제되지 않은 일기만 조회
				eqCursorId(cursorId)
			)
			.limit(SliceUtil.getLimit(pageable)) // limit 보다 데이터를 1개 더 들고와서, 해당 데이터가 있다면 hasNext 변수에 true 를 넣어 알림
			.orderBy(SliceUtil.getOrderSpecifier(pageable)) // 최신순 정렬
			.fetch();

		return SliceUtil.toSlice(pageable, diaryList, DiaryBriefResponse::from);
	}

	//동적 쿼리를 위한 BooleanExpression
	private BooleanExpression eqCursorId(Long cursorId) {
		return (cursorId == null) ? null : diary.id.lt(cursorId); // lt: 작다
	}

	// 삭제된 게시글인지 필터링
	private BooleanExpression eqIsDeleted(Boolean isDeleted) {
		return (isDeleted == null) ? null : diary.isDeleted.eq(isDeleted);
	}
}
