package com.example.server.controller;

import com.example.server.dto.ApiResponse;
import com.example.server.dto.MemberDto;
import com.example.server.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/profile")
    public ResponseEntity<Void> updateProfile(@RequestBody MemberDto.ProfileSaveRequest reqDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        memberService.updateProfile(reqDto, userDetails);
        return ApiResponse.success(null);
    }

}
