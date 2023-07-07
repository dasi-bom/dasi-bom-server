package com.example.server.config.jwt.dto;

import javax.persistence.Id;
import lombok.Getter;

@Getter
public class RefreshToken {

    @Id
    private String refreshToken;
    private String memberId; // socialId

    public RefreshToken(final String refreshToken, final String memberId) {
        this.refreshToken = refreshToken;
        this.memberId = memberId;
    }
}