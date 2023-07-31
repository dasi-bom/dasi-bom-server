package com.example.server.controller;

import static com.example.server.exception.ErrorCode.FILE_NOT_EXIST_ERROR;

import com.example.server.dto.ApiResponse;
import com.example.server.dto.MemberDto;
import com.example.server.exception.BusinessException;
import com.example.server.service.MemberService;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/profile")
    public ResponseEntity<Void> updateProfile(@RequestBody MemberDto.ProfileSaveRequest reqDto,
        @AuthenticationPrincipal UserDetails userDetails) {
        memberService.updateProfile(reqDto, userDetails.getUsername());
        return ApiResponse.success(null);
    }

    @PostMapping(value = "/profile/images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> uploadProfileImage(@RequestPart(required = false) MultipartFile multipartFile,
        @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        if (multipartFile == null) {
            throw new BusinessException(FILE_NOT_EXIST_ERROR);
        }
        memberService.uploadProfileImage(userDetails.getUsername(), multipartFile, "Profile/Member");

        return ApiResponse.success(null);
    }

}
