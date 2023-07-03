package com.example.server.service;

import com.example.server.dto.MemberDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface MemberService {

    void updateProfile(MemberDto.ProfileSaveRequest reqDto, UserDetails userDetails);
}
