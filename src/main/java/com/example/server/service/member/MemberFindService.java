package com.example.server.service.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.member.Member;
import com.example.server.exception.BusinessException;
import com.example.server.exception.ErrorCode;
import com.example.server.repository.member.MemberQueryRepository;

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
