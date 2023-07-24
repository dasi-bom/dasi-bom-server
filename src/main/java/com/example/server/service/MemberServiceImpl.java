package com.example.server.service;

import static com.example.server.exception.ErrorCode.CONFLICT_NICKNAME;
import static com.example.server.exception.ErrorCode.MEMBER_NOT_FOUND;

import com.example.server.domain.Image;
import com.example.server.domain.Member;
import com.example.server.dto.MemberDto;
import com.example.server.exception.CustomException;
import com.example.server.repository.MemberQueryRepository;
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

    private final S3Service s3Service;
    private final MemberQueryRepository memberQueryRepository;

    @Override
    @Transactional
    public void updateProfile(MemberDto.ProfileSaveRequest reqDto, UserDetails userDetails) {
        Member member = memberQueryRepository.findByProviderId(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        if (StringUtils.isNotBlank(reqDto.getNickname())) {
            validateDuplicatedNickname(reqDto.getNickname());
            member.updateProfileInfo(reqDto.getNickname());
        }
    }

    @Override
    @Transactional
    public void uploadProfileImage(UserDetails userDetails, MultipartFile multipartFile, String dirName) throws IOException {
        Member member = memberQueryRepository.findByProviderId(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        Image img = s3Service.uploadSingleImage(multipartFile, dirName);
        member.updateProfileImage(img);
    }

    private void validateDuplicatedNickname(String nickname) {
        if (memberQueryRepository.existsByNickname(nickname)) {
            throw new CustomException(CONFLICT_NICKNAME);
        }
    }
}
