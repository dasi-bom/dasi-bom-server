package com.example.server.domain.stamp.application;

import static com.example.server.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.stamp.api.dto.StampSaveRequest;
import com.example.server.domain.stamp.model.Stamp;
import com.example.server.domain.stamp.model.constants.StampType;
import com.example.server.domain.stamp.persistence.StampQueryRepository;
import com.example.server.domain.stamp.persistence.StampRepository;
import com.example.server.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StampService {

	private final StampQueryRepository stampQueryRepository;
	private final StampRepository stampRepository;

	@Transactional
	public void createStamp(StampSaveRequest stampSaveRequest) {
		StampType stampType = StampType.toEnum(stampSaveRequest.getStampType());
		if (stampQueryRepository.existsByStampType(stampType)) {
			throw new BusinessException(CONFLICT_STAMP);
		}
		stampRepository.save(Stamp.of(stampType));
	}
}
