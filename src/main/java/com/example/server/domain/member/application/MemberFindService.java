package com.example.server.domain.member.application;

import static com.example.server.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.member.model.Member;
import com.example.server.domain.member.persistence.MemberQueryRepository;
import com.example.server.global.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberFindService {

	private final MemberQueryRepository memberQueryRepository;

	public Member findMemberByUsername(String username) {
		return memberQueryRepository.findByProviderId(username)
			.orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
	}
}
