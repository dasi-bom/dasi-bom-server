package com.example.server.domain.stamp.application;

import static com.example.server.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.stamp.api.dto.StampSaveRequest;
import com.example.server.domain.stamp.model.Stamp;
import com.example.server.domain.stamp.model.constants.StampType;
import com.example.server.domain.stamp.persistence.StampRepository;
import com.example.server.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StampService {

	private final StampRepository stampRepository;

	@Transactional
	public Stamp createStamp(StampSaveRequest stampSaveRequest) {
		StampType stampType = StampType.toEnum(stampSaveRequest.getStampType());
		if (stampRepository.existsByStampType(stampType)) {
			throw new BusinessException(CONFLICT_STAMP);
		}
		Stamp stamp = Stamp.of(stampType);
		stampRepository.save(stamp);
		return stamp;
	}
}
