package com.example.server.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.example.server.dto.MemberDto;

public interface MemberService {

	void updateProfile(MemberDto.ProfileSaveRequest reqDto, String username);

	void uploadProfileImage(String username, MultipartFile multipartFile, String dirName) throws IOException;
	// void uploadProfileImage(UserDetails userDetails, MultipartFile multipartFile, String dirName) throws IOException;
}
