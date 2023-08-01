package com.example.server.domain.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.member.model.Member;
import com.example.server.global.exception.BusinessException;
import com.example.server.global.exception.ErrorCode;
import com.example.server.domain.member.persistence.MemberQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberFindService {

	private final MemberQueryRepository memberQueryRepository;

	public Member findByUserId(Long userId) {
		return memberQueryRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
	}
}
