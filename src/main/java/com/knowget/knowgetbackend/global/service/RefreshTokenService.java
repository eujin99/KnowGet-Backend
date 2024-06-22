package com.knowget.knowgetbackend.global.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.knowget.knowgetbackend.domain.user.service.UserService;
import com.knowget.knowgetbackend.global.config.security.JwtUtil;
import com.knowget.knowgetbackend.global.entity.RefreshToken;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;
	private final UserService userService;
	private final JwtUtil jwtUtil;

	public RefreshToken createRefreshToken(User user) {
		RefreshToken refreshToken = new RefreshToken(
			jwtUtil.generateRefreshToken(user.getUsername()),
			user,
			LocalDateTime.now().plusSeconds(jwtUtil.getRefreshTokenExpiration())
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

	public void deleteByUsername(String username) {
		User user = userService.findByUsername(username);
		refreshTokenRepository.deleteByUser(user);
	}

}
