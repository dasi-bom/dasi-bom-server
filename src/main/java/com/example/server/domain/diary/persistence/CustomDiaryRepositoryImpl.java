package com.example.server.domain.diary.persistence;

import static com.example.server.domain.diary.model.QDiary.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.example.server.domain.diary.api.dto.DiaryBriefResponse;
import com.example.server.domain.diary.model.Diary;
import com.example.server.domain.diary.model.condition.ReadCondition;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
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
			.limit(pageable.getPageSize() + 1) // limit 보다 데이터를 1개 더 들고와서, 해당 데이터가 있다면 hasNext 변수에 true 를 넣어 알림
			.orderBy(sortDiaryList(pageable)) // 최신순 정렬
			.fetch();

		List<DiaryBriefResponse> briefDiaryInfos = new ArrayList<>();
		for (Diary diary : diaryList) {
			briefDiaryInfos.add(DiaryBriefResponse.from(diary));
		}

		boolean hasNext = false;
		if (briefDiaryInfos.size() > pageable.getPageSize()) {
			briefDiaryInfos.remove(pageable.getPageSize());
			hasNext = true;
		}
		return new SliceImpl<>(briefDiaryInfos, pageable, hasNext);
	}

	// 특정 기준 정렬
	private OrderSpecifier<?> sortDiaryList(Pageable page) {
		// 서비스에서 보내준 Pageable 객체에 정렬조건 null 값 체크
		if (page.getSort().isEmpty()) {
			return new OrderSpecifier<>(Order.DESC, diary.createdDate);
		} else {
			// 정렬값이 들어 있으면 for 사용하여 값을 가져오기
			for (Sort.Order order : page.getSort()) {
				// 서비스에서 넣어준 DESC or ASC 를 가져오기
				Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
				// 서비스에서 넣어준 정렬 조건을 스위치 케이스 문을 활용하여 세팅
				switch (order.getProperty()) {
					case "updatedDate":
						return new OrderSpecifier<>(direction, diary.updatedDate);
				}
			}
		}
		return new OrderSpecifier<>(Order.DESC, diary.createdDate);
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
