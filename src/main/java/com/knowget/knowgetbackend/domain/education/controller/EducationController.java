package com.knowget.knowgetbackend.domain.education.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.knowget.knowgetbackend.domain.education.service.EducationService;
import com.knowget.knowgetbackend.global.entity.Education;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/education")
@RequiredArgsConstructor
public class EducationController {
	private final EducationService educationService;

	// 1. 모든 교육강의 가져오기
	@GetMapping
	public ResponseEntity<List<Education>> getAllEducations() {
		return ResponseEntity.ok(educationService.getAllEducations());
	}

	// 2. 교육강의 검색하기
	@GetMapping("/search")
	public ResponseEntity<List<Education>> searchEducations(@RequestParam String keyword) {
		return ResponseEntity.ok(educationService.searchEducations(keyword));
	}

	// 3. 모집중인 교육강의 가져오기
	@GetMapping("/recruiting")
	public ResponseEntity<List<Education>> getRecruitingEducations() {
		return ResponseEntity.ok(educationService.getRecruitingEducations());
	}
}
