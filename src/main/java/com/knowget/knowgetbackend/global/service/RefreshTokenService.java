package com.knowget.knowgetbackend.global.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knowget.knowgetbackend.global.config.security.JwtUtil;
import com.knowget.knowgetbackend.global.entity.RefreshToken;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtUtil jwtUtil;

	@Transactional
	public RefreshToken createRefreshToken(User user) {
		// 기존의 refresh token 제거
		refreshTokenRepository.deleteByUser(user);

		// 새로운 refresh token 생성
		RefreshToken refreshToken = new RefreshToken(
			jwtUtil.generateRefreshToken(user.getUsername()),
			user,
			LocalDateTime.now().plusSeconds(jwtUtil.getRefreshTokenExpiration() / 1000)
		);
		return refreshTokenRepository.save(refreshToken);
	}

	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	public void verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
			refreshTokenRepository.delete(token);
			throw new IllegalArgumentException("Refresh token was expired. Please make a new sign-in request");
		}
	}

	public RefreshToken updateRefreshToken(RefreshToken refreshToken) {
		refreshToken.setToken(jwtUtil.generateRefreshToken(refreshToken.getUser().getUsername()));
		refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(jwtUtil.getRefreshTokenExpiration()));
		return refreshTokenRepository.save(refreshToken);
	}

	@Transactional
	public void deleteByUsername(String username) {
		refreshTokenRepository.deleteByUser_Username(username);
	}

}
