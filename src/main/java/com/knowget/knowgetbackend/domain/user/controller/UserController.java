package com.knowget.knowgetbackend.domain.user.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.knowget.knowgetbackend.domain.user.dto.UserSignUpDTO;
import com.knowget.knowgetbackend.domain.user.service.UserService;
import com.knowget.knowgetbackend.global.config.security.JwtUtil;
import com.knowget.knowgetbackend.global.dto.AuthRequest;
import com.knowget.knowgetbackend.global.dto.AuthResponse;
import com.knowget.knowgetbackend.global.dto.RefreshTokenRequest;
import com.knowget.knowgetbackend.global.dto.ResultMessageDTO;
import com.knowget.knowgetbackend.global.entity.RefreshToken;
import com.knowget.knowgetbackend.global.service.AuthService;
import com.knowget.knowgetbackend.global.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final AuthService authService;
	private final JwtUtil jwtUtil;
	private final RefreshTokenService refreshTokenService;

	/**
	 * 사용자로부터 입력받은 ID의 중복여부 확인
	 *
	 * @param request 사용자 ID
	 * @return 중복 여부 (True or False)
	 * @author Jihwan
	 */
	@PostMapping("/check-username")
	public ResponseEntity<Boolean> checkUsername(@RequestBody Map<String, String> request) {
		String username = request.get("username");
		boolean exists = userService.checkUsername(username);
		return new ResponseEntity<>(exists, HttpStatus.OK);
	}

	/**
	 * 사용자 회원가입
	 *
	 * @param userSignUpDTO 사용자 회원가입 정보 (ID, 비밀번호, 선호지역, 선호직종)
	 * @return 회원가입 성공여부에 따른 결과 메시지
	 * @author Jihwan
	 */
	@PostMapping("/register")
	public ResponseEntity<ResultMessageDTO> register(@RequestBody UserSignUpDTO userSignUpDTO) {
		try {
			String msg = userService.register(userSignUpDTO);
			return new ResponseEntity<>(new ResultMessageDTO(msg), HttpStatus.OK);
		} catch (Exception e) {
			String errorMsg = "[Error] 회원가입 도중 오류가 발생했습니다 : " + e.getMessage();
			return new ResponseEntity<>(new ResultMessageDTO(errorMsg), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * 사용자 로그인
	 *
	 * @param authRequest 사용자가 입력한 ID, 비밀번호
	 * @return 로그인 성공여부에 따른 결과 메시지와 토큰
	 * @author Jihwan
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
		try {
			AuthResponse authResponse = authService.authenticate(authRequest.getUsername(), authRequest.getPassword());
			return new ResponseEntity<>(authResponse, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new ResultMessageDTO("[Error] 로그인 도중 오류가 발생했습니다 : " + e.getMessage()),
				HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * 토큰 갱신
	 *
	 * @param request 토큰 갱신 요청 (refreshToken)
	 * @return 갱신된 토큰 (accessToken, refreshToken)
	 * @author Jihwan
	 */
	@PostMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
		String refreshToken = request.getRefreshToken();

		if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ResultMessageDTO("유효하지 않은 refreshToken"));
		}

		String username = jwtUtil.getUsernameFromToken(request.getRefreshToken());
		RefreshToken token = refreshTokenService.findByToken(refreshToken)
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 refreshToken"));

		String newAccessToken = jwtUtil.generateAccessToken(username);
		return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
	}

}
