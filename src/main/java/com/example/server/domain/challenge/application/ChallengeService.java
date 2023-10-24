package com.example.server.domain.challenge.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.challenge.api.dto.ChallengeSaveRequest;
import com.example.server.domain.challenge.model.Challenge;
import com.example.server.domain.challenge.model.ChallengeType;
import com.example.server.domain.challenge.persistence.ChallengeRepository;
import com.example.server.domain.member.model.Member;
import com.example.server.domain.member.persistence.MemberRepository;
import com.example.server.global.exception.BusinessException;
import com.example.server.global.exception.errorcode.ChallengeErrorCode;
import com.example.server.global.exception.errorcode.MemberErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChallengeService {

	private final MemberRepository memberRepository;
	private final ChallengeRepository challengeRepository;

	@Transactional
	public Challenge createChallenge(String username, ChallengeSaveRequest challengeSaveRequest) {
		Member member = memberRepository.findByProviderId(username)
			.orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
		ChallengeType challengeType = ChallengeType.toEnum(challengeSaveRequest.getChallengeType());
		if (challengeRepository.existsByChallengeType(challengeType)) {
			throw new BusinessException(ChallengeErrorCode.CONFLICT_CHALLENGE);
		}
		Challenge challenge = Challenge.builder()
			.name(challengeSaveRequest.getName())
			.description(challengeSaveRequest.getDescription())
			.startDate(challengeSaveRequest.getStartDate())
			.endDate(challengeSaveRequest.getEndDate())
			.challengeType(challengeType)
			.admin(member)
			.build();
		challengeRepository.save(challenge);
		return challenge;
	}
}
