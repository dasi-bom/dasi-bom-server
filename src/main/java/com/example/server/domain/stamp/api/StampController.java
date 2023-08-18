package com.example.server.domain.stamp.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.domain.stamp.api.dto.StampResponse;
import com.example.server.domain.stamp.api.dto.StampSaveRequest;
import com.example.server.domain.stamp.application.StampService;
import com.example.server.domain.stamp.model.Stamp;
import com.example.server.global.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/stamp")
@RequiredArgsConstructor
public class StampController {

	private final StampService stampService;

	// @PreAuthorize("hasRole('ROLE_ADMIN')") // todo: ADMIN 만 가능
	@PostMapping()
	public ResponseEntity<StampResponse> createStamp(@Valid @RequestBody StampSaveRequest stampSaveRequest) {
		Stamp stamp = stampService.createStamp(stampSaveRequest);
		return ApiResponse.created(StampResponse.builder()
			.stampType(stamp.getStampType())
			.build());
	}
}