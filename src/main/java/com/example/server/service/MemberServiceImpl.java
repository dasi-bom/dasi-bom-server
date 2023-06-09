package com.example.server.service;

import static com.example.server.exception.ErrorCode.CONFLICT_NICKNAME;
import static com.example.server.exception.ErrorCode.MEMBER_NOT_FOUND;

import com.example.server.domain.Image;
import com.example.server.domain.Member;
import com.example.server.dto.MemberDto;
import com.example.server.exception.CustomException;
import com.example.server.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void updateProfile(MemberDto.ProfileSaveRequest reqDto, UserDetails userDetails) {
        Member member = memberRepository.findByProviderId(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        if (memberRepository.existsByNickname(reqDto.getNickname())) {
            throw new CustomException(CONFLICT_NICKNAME);
        }
        member.updateProfileInfo(reqDto.getNickname());
    }

    @Override
    @Transactional
    public void uploadProfileImage(UserDetails userDetails, MultipartFile multipartFile, String dirName) throws IOException {
        Member member = memberRepository.findByProviderId(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        Image img = s3Service.uploadSingleImage(multipartFile, dirName);
        member.updateProfileImage(img);
    }
}
