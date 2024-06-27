package com.knowget.knowgetbackend.domain.jobGuide.controller;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.knowget.knowgetbackend.domain.jobGuide.dto.JobGuideRequestDTO;
import com.knowget.knowgetbackend.domain.jobGuide.service.JobGuideService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/job-guide")
@RequiredArgsConstructor
public class JobGuideController {

	private final JobGuideService jobGuideService;

	/**
	 * 취업가이드 목록 조회
	 *
	 * @return responseEntity에 취업가이드 목록 담고, 반환함 !!
	 * @author 유진
	 */
	@GetMapping
	public ResponseEntity<?> getAllJobGuides() {
		try {
			return ResponseEntity.ok(jobGuideService.getAllJobGuides());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ArrayList<>());
		}
	}

	/**
	 * 특정 취업가이드 조회 (게시글 id로 조회함)
	 *
	 * @param id 조회할 취업가이드 게시글 id
	 * @return responseEntity에 해당 id 게시글 담아서 반환함 !!
	 * @author 유진
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getJobGuideById(@PathVariable Integer id) {
		return ResponseEntity.ok(jobGuideService.getJobGuideById(id));
	}

	/**
	 * 취업가이드 게시글 생성
	 *
	 * @param jobGuideRequestDTO 게시글 정보
	 * @return responseEntity에 생성된 게시글 담아서 반환.
	 * @author 유진
	 */
	@PostMapping
	public ResponseEntity<?> createJobGuide(@RequestBody JobGuideRequestDTO jobGuideRequestDTO) {
		return ResponseEntity.ok(jobGuideService.createJobGuide(jobGuideRequestDTO));
	}

	/**
	 * 취업가이드 게시글 수정
	 *
	 * @param id                 게시글 id
	 * @param jobGuideRequestDTO 수정할 게시글 정보
	 * @return responseEntity에 수정된 게시글 담아 반환
	 * @author 유진
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> updateJobGuide(@PathVariable Integer id,
		@RequestBody JobGuideRequestDTO jobGuideRequestDTO) {
		return ResponseEntity.ok(jobGuideService.updateJobGuide(id, jobGuideRequestDTO));
	}

	/**
	 * 취업가이드 게시글 삭제
	 *
	 * @param id 삭제할 게시글 id
	 * @return entity에 삭제 완료 상태 반환함..
	 * @author 유진
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteJobGuide(@PathVariable Integer id) {
		jobGuideService.deleteJobGuide(id);
		return ResponseEntity.noContent().build();
	}
}
