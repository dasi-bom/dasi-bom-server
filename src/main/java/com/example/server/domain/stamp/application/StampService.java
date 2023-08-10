package com.example.server.domain.stamp.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.member.application.MemberFindService;
import com.example.server.domain.stamp.api.dto.StampSaveRequest;
import com.example.server.domain.stamp.model.Stamp;
import com.example.server.domain.stamp.model.constants.StampType;
import com.example.server.domain.stamp.persistence.StampQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StampService {

	private final MemberFindService memberFindService;
	private final StampQueryRepository stampQueryRepository;

	// @Transactional
	// public void createStamp(String username, StampSaveRequest stampSaveRequest) {
	//
	// 	Member member = memberFindService.findMemberByProviderId(username);
	// 	// if (member.getRole() != RoleType.ROLE_ADMIN) {
	// 	//	 // 권한없음 exception
	// 	// }
	//
	// 	Stamp stamp = Stamp.of(stampSaveRequest.getStampType());
	// 	stampQueryRepository.save(stamp);
	// }

	@Transactional
	public void createStamp(StampSaveRequest stampSaveRequest) {
		System.out.println("-------> Stamp Service");
		System.out.println(stampSaveRequest.getStampType());
		Stamp stamp = Stamp.of(StampType.toEnum(stampSaveRequest.getStampType()));
		stampQueryRepository.save(stamp);
	}
}
