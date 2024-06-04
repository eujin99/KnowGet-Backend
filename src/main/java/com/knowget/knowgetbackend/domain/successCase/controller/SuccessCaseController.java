package com.knowget.knowgetbackend.domain.successCase.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.knowget.knowgetbackend.domain.successCase.dto.SuccessCaseRequestDTO;
import com.knowget.knowgetbackend.domain.successCase.dto.SuccessCaseResponseDTO;
import com.knowget.knowgetbackend.domain.successCase.service.SuccessCaseService;
import com.knowget.knowgetbackend.global.dto.ResultMessageDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/success-case")
@RequiredArgsConstructor
public class SuccessCaseController {

	private final SuccessCaseService successCaseService;

	// 1. 특정 caseId에 해당하는 SuccessCase 조회
	// postman 사용법 : get으로 요청 route : http://localhost:8080/api/v1/success-case/{caseId}
	@GetMapping("/{caseId}")
	public ResponseEntity<SuccessCaseResponseDTO> getSuccessCase(@PathVariable Integer caseId) {
		SuccessCaseResponseDTO successCase = successCaseService.getSuccessCase(caseId);
		return ResponseEntity.ok(successCase);
	}

	// 2. 전체 SuccessCase 목록 조회
	// postman 사용법 : get으로 요청 route : http://localhost:8080/api/v1/success-case
	@GetMapping
	public ResponseEntity<List<SuccessCaseResponseDTO>> getAllSuccessCases() {
		List<SuccessCaseResponseDTO> successCases = successCaseService.getAllSuccessCases();
		return ResponseEntity.ok(successCases);
	}

	// 3. SuccessCase 생성
	// postman 사용법 : username / title / content 입력 후 post로 요청 : http://localhost:8080/api/v1/success-case
	// 성공 시 200 OK 반환
	@PostMapping
	public ResponseEntity<SuccessCaseResponseDTO> createSuccessCase(
		@RequestBody SuccessCaseRequestDTO successCaseRequestDTO) {
		SuccessCaseResponseDTO successCase = successCaseService.createSuccessCase(successCaseRequestDTO);
		return ResponseEntity.ok(successCase);
	}

	// 4. SuccessCase 삭제
	// postman 사용법 : delete로 요청 route : http://localhost:8080/api/v1/success-case/{caseId}
	@DeleteMapping("/{caseId}")
	public ResponseEntity<ResultMessageDTO> deleteSuccessCase(@PathVariable Integer caseId) {
		ResultMessageDTO resultMessage = successCaseService.deleteSuccessCase(caseId);
		return ResponseEntity.ok(resultMessage);
	}

	// 5. SuccessCase 검색 - By Using "Keyword"
	// postman 사용법 : get으로 요청 route : http://localhost:8080/api/v1/success-case/search?keyword={keyword}
	@GetMapping("/search")
	public ResponseEntity<List<SuccessCaseResponseDTO>> searchSuccessCase(@RequestParam String keyword) {
		List<SuccessCaseResponseDTO> successCases = successCaseService.searchSuccessCase(keyword);
		return ResponseEntity.ok(successCases);
	}

	// 6. SuccessCase 승인상태 업데이트
	// postman 사용법 : Patch으로 요청 route : http://localhost:8080/api/v1/success-case/approval/{caseId}
	@PatchMapping("/approval/{caseId}")
	public ResponseEntity<Short> updateSuccessCaseApproval(@PathVariable Integer caseId,
		@RequestParam Short status) {
		Short updatedStatus = successCaseService.updateSuccessCaseApproval(caseId, status);
		return ResponseEntity.ok(updatedStatus);
	}
}
