package com.example.server.global.util;

import static com.example.server.domain.diary.model.QDiary.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

import com.example.server.domain.diary.api.dto.DiaryBriefResponse;
import com.example.server.domain.diary.model.Diary;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;

public class SliceUtil {
	/**
	 * 페이징 정보로부터 limit 값 가져오기
	 */
	public static int getLimit(Pageable pageable) {
		return pageable.getPageSize() + 1;
	}

	public static OrderSpecifier<?> sortDiaryList(Pageable pageable) {
		// 서비스에서 보내준 Pageable 객체에 정렬조건 null 값 체크
		if (pageable.getSort().isEmpty()) {
			return new OrderSpecifier<>(Order.DESC, diary.createdDate);
		} else {
			// 정렬값이 들어 있으면 for 사용하여 값을 가져오기
			for (Sort.Order order : pageable.getSort()) {
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

	/**
	 * 리스트를 Slice로 변환
	 */
	public static SliceImpl<DiaryBriefResponse> toSlice(Pageable pageable, List<Diary> diaryList) {
		List<DiaryBriefResponse> briefDiaryInfos = new ArrayList<>();
		for (Diary diary : diaryList) {
			briefDiaryInfos.add(DiaryBriefResponse.from(diary));
		}
		boolean hasNext = briefDiaryInfos.size() > pageable.getPageSize();
		if (hasNext) {
			briefDiaryInfos.remove(pageable.getPageSize());
		}
		return new SliceImpl<>(briefDiaryInfos, pageable, hasNext);
	}
}
