package com.example.server.domain.challenge.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.domain.challenge.api.dto.ChallengeResponse;
import com.example.server.domain.challenge.api.dto.ChallengeSaveRequest;
import com.example.server.domain.challenge.application.ChallengeService;
import com.example.server.domain.challenge.model.Challenge;
import com.example.server.global.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/challenge")
@RequiredArgsConstructor
public class ChallengeController {

	private final ChallengeService challengeService;

	// 챌린지 등록
	// @PreAuthorize("hasRole('ROLE_ADMIN')") // todo: ADMIN 만 가능
	@PostMapping()
	public ResponseEntity<ChallengeResponse> createStamp(
		@AuthenticationPrincipal UserDetails userDetails,
		@Valid @RequestBody ChallengeSaveRequest challengeSaveRequest) {
		Challenge challenge = challengeService.createChallenge(userDetails.getUsername(), challengeSaveRequest);
		return ApiResponse.created(ChallengeResponse.builder()
			.name(challenge.getName())
			.challengeType(challenge.getChallengeType().getMessage())
			.registeredBy(challenge.getAdmin().getName())
			.build());
	}
}
