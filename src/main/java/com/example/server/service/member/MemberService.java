package com.example.server.service.member;

import com.example.server.dto.MemberDto;
import java.io.IOException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {

    void updateProfile(MemberDto.ProfileSaveRequest reqDto, UserDetails userDetails);
    void uploadProfileImage(UserDetails userDetails, MultipartFile multipartFile, String dirName) throws IOException;
}
