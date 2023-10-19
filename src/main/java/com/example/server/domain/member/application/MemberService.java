package com.example.server.domain.member.application;

import static com.example.server.global.exception.ErrorCode.*;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.domain.image.model.Image;
import com.example.server.domain.member.api.dto.MemberProfileSaveRequest;
import com.example.server.domain.member.model.Member;
import com.example.server.domain.member.persistence.MemberRepository;
import com.example.server.global.exception.BusinessException;
import com.example.server.global.util.S3Uploader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	static final String MEMBER_PROFILE_DIR_NAME = "Profile/Member";
	private final S3Uploader s3Uploader;
	private final MemberRepository memberRepository;

	@Transactional
	public Member updateProfile(MemberProfileSaveRequest reqDto, String username) {
		Member member = memberRepository.findByProviderId(username)
			.orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
		validateDuplicatedNickname(reqDto.getNickname());
		member.updateProfileInfo(reqDto.getNickname());
		return member;
	}

	@Transactional
	public Member uploadProfileImage(String username, MultipartFile multipartFile) throws IOException {
		Member member = memberRepository.findByProviderId(username)
			.orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
		Image img = s3Uploader.uploadSingleImage(multipartFile, MEMBER_PROFILE_DIR_NAME);
		member.updateProfileImage(img);
		return member;
	}

	private void validateDuplicatedNickname(String nickname) {
		if (memberRepository.existsByNickname(nickname)) {
			throw new BusinessException(CONFLICT_NICKNAME);
		}
	}
}
