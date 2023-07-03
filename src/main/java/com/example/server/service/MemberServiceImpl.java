package com.example.server.service;

import static com.example.server.exception.ErrorCode.MEMBER_NOT_FOUND;

import com.example.server.domain.Member;
import com.example.server.dto.MemberDto;
import com.example.server.exception.CustomException;
import com.example.server.repository.MemberRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void updateProfile(MemberDto.ProfileSaveRequest reqDto, UserDetails userDetails) {
        Member member = memberRepository.findByProviderId(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        member.updateProfile(reqDto.getNickname());
    }
}
