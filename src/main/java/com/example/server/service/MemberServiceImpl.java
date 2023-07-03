package com.example.server.service;

import com.example.server.domain.Member;
import com.example.server.dto.MemberDto;
import com.example.server.repository.MemberRepository;
import java.util.Optional;
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
        System.out.println(userDetails.getUsername());
        Optional<Member> oMember = memberRepository.findByProviderId(userDetails.getUsername());

        if (oMember.isPresent()) {
            Member member = oMember.get();
            member.updateProfile(reqDto.getNickname());
        }
    }
}
