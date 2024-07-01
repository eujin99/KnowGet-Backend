package com.knowget.knowgetbackend.domain.education.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.knowget.knowgetbackend.domain.education.dto.EducationResponseDTO;
import com.knowget.knowgetbackend.domain.education.service.EducationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/education")
@RequiredArgsConstructor
public class EducationController {
	private final EducationService educationService;

	// 1. 모든 교육강의 가져오기
	@GetMapping
	public ResponseEntity<List<EducationResponseDTO>> getAllEducations() {
		return ResponseEntity.ok(educationService.getAllEducations());
	}

	// 2. 교육강의 검색하기
	@GetMapping("/search")
	public ResponseEntity<List<EducationResponseDTO>> searchEducations(@RequestParam String keyword) {
		return ResponseEntity.ok(educationService.searchEducations(keyword));
	}

	// 3. 모집중인 교육강의 가져오기
	@GetMapping("/recruiting")
	public ResponseEntity<List<EducationResponseDTO>> getRecruitingEducations() {
		return ResponseEntity.ok(educationService.getRecruitingEducations());
	}

	@GetMapping("/2")
	public ResponseEntity<List<EducationResponseDTO>> getAllEducations2() {
		return ResponseEntity.ok(educationService.getAllEducations2());
	}

	@GetMapping("/fetch-educations")
	public ResponseEntity<Void> fetchEducations() {
		log.info("education fetching started at {}", LocalDateTime.now());
		int insertCount = educationService.fetchEducations(1, 1000);
		log.info("fetched {} educations at {}", insertCount, LocalDateTime.now());
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
