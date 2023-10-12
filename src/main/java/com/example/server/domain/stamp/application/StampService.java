package com.example.server.domain.stamp.application;

import static com.example.server.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.member.model.Member;
import com.example.server.domain.member.persistence.MemberRepository;
import com.example.server.domain.stamp.api.dto.StampSaveRequest;
import com.example.server.domain.stamp.model.Stamp;
import com.example.server.domain.stamp.persistence.StampRepository;
import com.example.server.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StampService {

	private final MemberRepository memberRepository;
	private final StampRepository stampRepository;

	@Transactional
	public Stamp createStamp(String username, StampSaveRequest stampSaveRequest) {
		Member member = memberRepository.findByProviderId(username)
			.orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
		// StampType stampType = StampType.toEnum(stampSaveRequest.getStampType());
		// if (stampRepository.existsByStampType(stampType)) {
		// 	throw new BusinessException(CONFLICT_STAMP);
		// }
		Stamp stamp = Stamp.builder()
			.name(stampSaveRequest.getName())
			.admin(member)
			.build();
		stampRepository.save(stamp);
		return stamp;
	}
}
