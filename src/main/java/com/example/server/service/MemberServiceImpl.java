package com.example.server.service;

import static com.example.server.exception.ErrorCode.CONFLICT_NICKNAME;
import static com.example.server.exception.ErrorCode.MEMBER_NOT_FOUND;

import com.example.server.domain.Image;
import com.example.server.domain.Member;
import com.example.server.dto.MemberDto;
import com.example.server.exception.BusinessException;
import com.example.server.repository.MemberQueryRepository;
import com.example.server.util.S3Uploader;
import com.nimbusds.oauth2.sdk.util.StringUtils;

import java.io.IOException;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final S3Uploader s3Uploader;
    private final MemberQueryRepository memberQueryRepository;

    @Override
    @Transactional
    public void updateProfile(MemberDto.ProfileSaveRequest reqDto, String username) {
        Member member = memberQueryRepository.findByProviderId(username)
            .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        if (StringUtils.isNotBlank(reqDto.getNickname())) {
            validateDuplicatedNickname(reqDto.getNickname());
            member.updateProfileInfo(reqDto.getNickname());
        }
    }

    @Override
    @Transactional
    public void uploadProfileImage(String username, MultipartFile multipartFile, String dirName) throws IOException {
        Member member = memberQueryRepository.findByProviderId(username)
            .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        Image img = s3Uploader.uploadSingleImage(multipartFile, dirName);
        member.updateProfileImage(img);
    }

    private void validateDuplicatedNickname(String nickname) {
        if (memberQueryRepository.existsByNickname(nickname)) {
            throw new BusinessException(CONFLICT_NICKNAME);
        }
    }
}
