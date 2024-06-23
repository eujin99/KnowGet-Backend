package com.knowget.knowgetbackend.global.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knowget.knowgetbackend.domain.user.service.UserService;
import com.knowget.knowgetbackend.global.config.security.JwtUtil;
import com.knowget.knowgetbackend.global.dto.AuthResponse;
import com.knowget.knowgetbackend.global.entity.RefreshToken;
import com.knowget.knowgetbackend.global.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final UserService userService;
	private final RefreshTokenService refreshTokenService;

	@Transactional
	public AuthResponse authenticate(String username, String password) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(username, password));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		User user = userService.findByUsername(username);
		String accessToken = jwtUtil.generateAccessToken(username);

		// 기존 refresh token 삭제
		refreshTokenService.deleteByUsername(user.getUsername());

		RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

		return new AuthResponse(username, user.getRole(), accessToken, refreshToken.getToken());
	}

	@Transactional
	public AuthResponse refreshToken(String refreshToken) {
		RefreshToken token = refreshTokenService.findByToken(refreshToken)
			.orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

		refreshTokenService.verifyExpiration(token);

		String newAccessToken = jwtUtil.generateAccessToken(token.getUser().getUsername());
		RefreshToken newRefreshToken = refreshTokenService.updateRefreshToken(token);

		return new AuthResponse(token.getUser().getUsername(), token.getUser().getRole(), newAccessToken,
			newRefreshToken.getToken());
	}

}
