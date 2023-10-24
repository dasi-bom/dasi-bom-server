package com.example.server.global.jwt.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.global.exception.BusinessException;
import com.example.server.global.exception.errorcode.AuthErrorCode;
import com.example.server.global.jwt.dao.RefreshTokenDao;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {

	private final RefreshTokenDao refreshTokenDao;

	public void save(String providerId, String refreshToken) {
		refreshTokenDao.saveRefreshToken(providerId, refreshToken);
	}

	public String findById(String providerId) {
		return refreshTokenDao.getRefreshToken(providerId)
			.orElseThrow(() -> {

				throw new BusinessException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND);
			}).toString();
	}

	public void delete(String providerId) {
		refreshTokenDao.removeRefreshToken(providerId);
	}

}
