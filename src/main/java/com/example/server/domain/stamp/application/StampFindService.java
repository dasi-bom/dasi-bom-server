package com.example.server.domain.stamp.application;

import static com.example.server.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.stamp.model.Stamp;
import com.example.server.domain.stamp.model.constants.StampType;
import com.example.server.domain.stamp.persistence.StampQueryRepository;
import com.example.server.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StampFindService {

	private final StampQueryRepository stampQueryRepository;

	public Stamp findByStampType(StampType stampType) {
		return stampQueryRepository.findByStampType(stampType)
			.orElseThrow(() -> new BusinessException(STAMP_INVALID));
	}
}
