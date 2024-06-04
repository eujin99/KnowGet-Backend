package com.knowget.knowgetbackend.domain.successCase.service;

import java.util.List;

import com.knowget.knowgetbackend.domain.successCase.dto.SuccessCaseRequestDTO;
import com.knowget.knowgetbackend.domain.successCase.dto.SuccessCaseResponseDTO;

public interface SuccessCaseService {

	// 특정 caseId에 해당하는 SuccessCase 조회
	SuccessCaseResponseDTO getSuccessCase(Integer caseId);

	// 전체 SuccessCase 목록 조회
	List<SuccessCaseResponseDTO> getAllSuccessCases();

	// SuccessCase 생성
	SuccessCaseResponseDTO createSuccessCase(SuccessCaseRequestDTO successCaseRequestDTO);

	// SuccessCase 삭제
	String deleteSuccessCase(Integer caseId);

	// SuccessCase 검색 - By Using "Keyword"
	List<SuccessCaseResponseDTO> searchSuccessCase(String keyword);

	// SuccessCase 승인상태 업데이트
	String updateSuccessCaseApproval(Integer caseId, Short status);

}
