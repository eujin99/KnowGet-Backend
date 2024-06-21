package com.knowget.knowgetbackend.domain.counseling.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.knowget.knowgetbackend.domain.counseling.dto.CounselingRequestDTO;
import com.knowget.knowgetbackend.domain.counseling.dto.CounselingResponseDTO;
import com.knowget.knowgetbackend.domain.counseling.service.CounselingService;
import com.knowget.knowgetbackend.global.dto.ResultMessageDTO;
import com.knowget.knowgetbackend.global.exception.RequestFailedException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/counseling")
public class CounselingController {

	private final CounselingService counselingService;

	/**
	 * 최신 순으로 상담 목록 조회
	 *
	 * @return ResponseEntity<List < CounselingResponseDTO>> 상담 목록
	 * @author 근엽
	 */
	@GetMapping
	public ResponseEntity<List<CounselingResponseDTO>> getAllCounseling() {
		List<CounselingResponseDTO> counselingList = counselingService.getAllCounseling();

		return new ResponseEntity<>(counselingList, HttpStatus.OK);
	}

	/**
	 * 상담 상세 조회
	 *
	 * @param id
	 * @return ResponseEntity<CounselingResponseDTO> 상담 내용
	 * @author 근엽
	 */
	@PostMapping("/{id}")
	public ResponseEntity<CounselingResponseDTO> getCounseling(@PathVariable Integer id) {
		CounselingResponseDTO counseling = counselingService.getCounselingById(id);

		return new ResponseEntity<>(counseling, HttpStatus.OK);
	}

	/**
	 * 상담 작성
	 *
	 * @param counselingRequestDTO
	 * @return ResponseEntity<String> 작성 완료 메시지
	 * @author 근엽
	 */
	@PostMapping
	public ResponseEntity<ResultMessageDTO> saveCounseling(
		@RequestBody CounselingRequestDTO counselingRequestDTO) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new RequestFailedException("로그인이 필요합니다");
		}
		String username = authentication.getName();

		counselingRequestDTO.setUsername(username);

		String message = counselingService.saveCounseling(counselingRequestDTO);

		return new ResponseEntity<>(new ResultMessageDTO(message), HttpStatus.OK);
	}
}
