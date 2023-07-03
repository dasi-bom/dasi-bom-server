package com.example.server.dto;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProfileSaveRequest {

        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        private String nickname;

    }
}
