package com.example.server.domain.member.application;

import static com.example.server.global.exception.ErrorCode.*;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.domain.image.model.Image;
import com.example.server.domain.member.api.dto.MemberProfileSaveRequest;
import com.example.server.domain.member.model.Member;
import com.example.server.domain.member.persistence.MemberQueryRepository;
import com.example.server.global.exception.BusinessException;
import com.example.server.global.util.S3Uploader;
import com.nimbusds.oauth2.sdk.util.StringUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final S3Uploader s3Uploader;
	private final MemberQueryRepository memberQueryRepository;
	static final String MEMBER_PROFILE_DIR_NAME = "Profile/Member";

	@Transactional
	public Member updateProfile(MemberProfileSaveRequest reqDto, String username) {
		Member member = memberQueryRepository.findByProviderId(username)
			.orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
		if (StringUtils.isNotBlank(reqDto.getNickname())) {
			validateDuplicatedNickname(reqDto.getNickname());
			member.updateProfileInfo(reqDto.getNickname());
		}
		return member;
	}

	@Transactional
	public void uploadProfileImage(String username, MultipartFile multipartFile) throws IOException {
		Member member = memberQueryRepository.findByProviderId(username)
			.orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
		Image img = s3Uploader.uploadSingleImage(multipartFile, MEMBER_PROFILE_DIR_NAME);
		member.updateProfileImage(img);
		return member;
	}

	private void validateDuplicatedNickname(String nickname) {
		if (memberQueryRepository.existsByNickname(nickname)) {
			throw new BusinessException(CONFLICT_NICKNAME);
		}
	}
}
