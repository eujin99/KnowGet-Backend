package com.knowget.knowgetbackend.domain.successCase.service;

import java.util.List;

import com.knowget.knowgetbackend.domain.successCase.dto.SuccessCaseRequestDTO;
import com.knowget.knowgetbackend.domain.successCase.dto.SuccessCaseResponseDTO;
import com.knowget.knowgetbackend.global.dto.ResultMessageDTO;

public interface SuccessCaseService {

	// 1. 특정 caseId에 해당하는 SuccessCase 조회
	SuccessCaseResponseDTO getSuccessCase(Integer caseId);

	// 2. 전체 SuccessCase 목록 조회
	List<SuccessCaseResponseDTO> getAllSuccessCases();

	// 3. SuccessCase 생성
	ResultMessageDTO createSuccessCase(SuccessCaseRequestDTO successCaseRequestDTO);

	// 4. SuccessCase 삭제
	ResultMessageDTO deleteSuccessCase(Integer caseId);

	// 5. SuccessCase 검색 - By Using "Keyword"
	List<SuccessCaseResponseDTO> searchSuccessCase(String keyword);

	// 6. SuccessCase 승인상태 업데이트
	String updateSuccessCaseApproval(Integer caseId, Integer status);

}
