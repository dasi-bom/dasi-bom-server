package com.example.server.service;

import static com.example.server.exception.ErrorCode.*;

import java.io.IOException;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.domain.Image;
import com.example.server.domain.Member;
import com.example.server.dto.MemberDto;
import com.example.server.exception.CustomException;
import com.example.server.repository.MemberRepository;
import com.example.server.util.S3Uploader;
import com.nimbusds.oauth2.sdk.util.StringUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final S3Uploader s3Uploader;
	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public void updateProfile(MemberDto.ProfileSaveRequest reqDto, String username) {
		Member member = memberRepository.findByProviderId(username)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
		if (StringUtils.isNotBlank(reqDto.getNickname())) {
			validateDuplicatedNickname(reqDto.getNickname());
			member.updateProfileInfo(reqDto.getNickname());
		}
	}

	@Override
	@Transactional
	public void uploadProfileImage(String username, MultipartFile multipartFile, String dirName) throws
		IOException {
		Member member = memberRepository.findByProviderId(username)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
		Image img = s3Uploader.uploadSingleImage(multipartFile, dirName);
		member.updateProfileImage(img);
	}

	private void validateDuplicatedNickname(String nickname) {
		if (memberRepository.existsByNickname(nickname)) {
			throw new CustomException(CONFLICT_NICKNAME);
		}
	}
}
