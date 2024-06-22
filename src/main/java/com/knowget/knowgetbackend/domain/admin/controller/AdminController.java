package com.knowget.knowgetbackend.domain.admin.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.knowget.knowgetbackend.domain.admin.dto.AdminModifyDTO;
import com.knowget.knowgetbackend.domain.admin.dto.AdminResponseDTO;
import com.knowget.knowgetbackend.domain.admin.service.AdminService;
import com.knowget.knowgetbackend.global.dto.ResultMessageDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

	private final AdminService adminService;

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

}
