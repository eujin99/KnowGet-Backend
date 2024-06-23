package com.knowget.knowgetbackend.domain.admin.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.knowget.knowgetbackend.domain.admin.dto.AdminModifyDTO;
import com.knowget.knowgetbackend.domain.admin.dto.AdminResponseDTO;
import com.knowget.knowgetbackend.domain.admin.dto.AdminSignupDTO;
import com.knowget.knowgetbackend.domain.admin.service.AdminService;
import com.knowget.knowgetbackend.global.dto.AuthRequest;
import com.knowget.knowgetbackend.global.dto.AuthResponse;
import com.knowget.knowgetbackend.global.dto.ResultMessageDTO;
import com.knowget.knowgetbackend.global.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

	private final AdminService adminService;
	private final AuthService authService;

	/**
	 * 회원 목록 조회
	 *
	 * @return ResponseEntity<List < User>>
	 * @author 근엽
	 */
	@GetMapping("/users")
	public ResponseEntity<List<AdminResponseDTO>> getUsers() {
		List<AdminResponseDTO> users = adminService.getAllUsers();

		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	/**
	 * 회원 상태 변경
	 *
	 * @param userId         사용자 ID
	 * @param adminModifyDTO 변경할 사용자 상태
	 * @return ResponseEntity<ResultMessageDTO> 변경된 회원 상태
	 */
	@PatchMapping("/user/{userId}")
	public ResponseEntity<ResultMessageDTO> updateIsActive(@PathVariable("userId") Integer userId,
		@RequestBody AdminModifyDTO adminModifyDTO) {
		String message = adminService.updateIsActive(userId, adminModifyDTO.getIsActive());

		return new ResponseEntity<>(new ResultMessageDTO(message), HttpStatus.OK);

	}

	/**
	 * 관리자 회원가입
	 *
	 * @param adminSignupDTO 관리자 회원가입 DTO (아이디, 비밀번호)
	 * @return ResponseEntity<ResultMessageDTO> 회원가입 결과 메시지
	 * @author Jihwan
	 */
	@PostMapping("/register")
	public ResponseEntity<ResultMessageDTO> register(@RequestBody AdminSignupDTO adminSignupDTO) {
		try {
			adminSignupDTO.setPrefLocation("NULL");
			adminSignupDTO.setPrefJob("NULL");
			String msg = adminService.register(adminSignupDTO);
			return new ResponseEntity<>(new ResultMessageDTO(msg), HttpStatus.OK);
		} catch (Exception e) {
			String errorMsg = "[Error] 관리자 가입 도중 오류가 발생했습니다 : " + e.getMessage();
			return new ResponseEntity<>(new ResultMessageDTO(errorMsg), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * 관리자 로그인
	 *
	 * @param authRequest 로그인 요청 DTO (아이디, 비밀번호)
	 * @return ResponseEntity<?> 로그인 결과 (성공 시 토큰, 실패 시 에러 메시지)
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

}
