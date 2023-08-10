package com.example.server.domain.stamp.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.domain.stamp.api.dto.StampSaveRequest;
import com.example.server.domain.stamp.application.StampService;
import com.example.server.global.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/stamp")
@RequiredArgsConstructor
public class StampController {

	private final StampService stampService;

	// 스탬프 등록
	// todo: ADMIN 만 가능
	// @PostMapping()
	// public ResponseEntity<Void> createStamp(@AuthenticationPrincipal UserDetails userDetails,
	// 	@Valid StampSaveRequest stampSaveRequest) {
	// 	stampService.createStamp(userDetails.getUsername(), stampSaveRequest);
	// 	return ApiResponse.success(null);
	// }

	@PostMapping()
	public ResponseEntity<Void> createStamp(@Valid StampSaveRequest stampSaveRequest) {
		System.out.println("-------> Stamp Controller");
		stampService.createStamp(stampSaveRequest);
		return ApiResponse.success(null);
	}
}
