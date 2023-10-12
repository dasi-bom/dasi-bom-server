package com.example.server.domain.stamp.api.dto;

import static lombok.AccessLevel.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.example.server.domain.stamp.model.constants.StampType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 스탬프 조회에 사용되는 dto
 */
@Builder
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class StampResponse {

	@Enumerated(EnumType.STRING)
	private StampType stampType;

}
