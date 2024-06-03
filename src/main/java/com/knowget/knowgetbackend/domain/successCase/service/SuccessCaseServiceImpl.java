package com.knowget.knowgetbackend.domain.successCase.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knowget.knowgetbackend.domain.successCase.dto.SuccessCaseRequestDTO;
import com.knowget.knowgetbackend.domain.successCase.dto.SuccessCaseResponseDTO;
import com.knowget.knowgetbackend.domain.successCase.exception.ResourceNotFoundException;
import com.knowget.knowgetbackend.domain.successCase.repository.SuccessCaseRepository;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.SuccessCase;
import com.knowget.knowgetbackend.global.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SuccessCaseServiceImpl implements SuccessCaseService {

	private final SuccessCaseRepository successCaseRepository;
	private final UserRepository userRepository;

	/**
	 * 1. 특정 casdId에 해당하는 SuccessCase 조회
	 *
	 * @param caseId 취업 성공사례 게시글 ID
	 * @return caseId에 해당하는 SuccessCase 전체
	 * @author MJ
	 */

	// 1. 특정 caseId에 해당하는 SuccessCase 조회 - findById
	@Override
	@Transactional(readOnly = true)
	public SuccessCaseResponseDTO getSuccessCase(Integer caseId) {
		Optional<SuccessCase> successCase = successCaseRepository.findById(caseId);
		return successCase.map(SuccessCaseResponseDTO::new).orElse(null);
		//caseId에 해당하는 successCase를 반환!
	}

	/**
	 * 2. 특정 취업 성공사례 게시글에 달린 모든 댓글 조회
	 *
	 * @return 전체 취업 성공사례 목록 리스트
	 * @author MJ
	 */

	// 2. 전체 SuccessCase 목록 조회 - findAll
	@Override
	@Transactional(readOnly = true)
	public List<SuccessCaseResponseDTO> getAllSuccessCases() {
		return successCaseRepository.findAll().stream().map(SuccessCaseResponseDTO::new).toList();
		//모든 successcase를 반환!
	}

	/**
	 * 3. SuccessCase 생성
	 *
	 * @param successCaseRequestDTO username : 사용자 ID, title : 게시글 제목, content : 게시글 내용
	 * @return SuccessCaseResponseDTO
	 * @author MJ
	 */

	// 3. SuccessCase 생성 - save
	@Override
	@Transactional
	public SuccessCaseResponseDTO createSuccessCase(SuccessCaseRequestDTO successCaseRequestDTO) {
		User user = userRepository.findByUsername(successCaseRequestDTO.getUsername())
			.orElse(null);

		SuccessCase successCase = SuccessCase.builder()
			.user(user)
			.title(successCaseRequestDTO.getTitle())
			.content(successCaseRequestDTO.getContent())
			.build();

		//SuccessCase에 저장
		successCaseRepository.save(successCase);
		return new SuccessCaseResponseDTO(successCase);
	}

	/**
	 * 4. SuccessCase 삭제
	 *
	 * @param caseId 취업 성공사례 게시글 ID
	 * @return 삭제 성공 여부 메시지
	 * @throws ResourceNotFoundException 존재하지 않는 게시글일 경우
	 * @author MJ
	 */

	// 4. SuccessCase 삭제
	@Override
	@Transactional
	public String deleteSuccessCase(Integer caseId) {
		// 존재하는지 확인
		if (!successCaseRepository.existsById(caseId)) {
			throw new ResourceNotFoundException("등록된" + caseId + "번 성공사례가 없습니다.");
		}

		// 삭제
		successCaseRepository.deleteById(caseId);
		return "해당 글이 삭제 되었습니다";
	}

	/**
	 * 5. SuccessCase 검색 - By Using "Keyword"
	 *
	 * @param keyword 검색 키워드
	 * @return keyword가 들어있는 성공사례 검색 결과 리스트
	 * @author MJ
	 */

	// 5. SuccessCase 검색 - By Using "Keyword"
	@Override
	@Transactional(readOnly = true)
	public List<SuccessCaseResponseDTO> searchSuccessCase(String keyword) {
		List<SuccessCase> successCases = successCaseRepository.findByTitleContaining(keyword);
		return successCases.stream().map(SuccessCaseResponseDTO::new).collect(Collectors.toList());
	}

	/**
	 * 6. SuccessCase 승인상태 업데이트
	 *
	 * @param caseId 취업 성공사례 게시글 ID, status : 승인 상태
	 * @return 업데이트된 승인 상태(Return Value is gonna be... 2,3)
	 * @author MJ
	 */

	// 6. SuccessCase 승인상태 업데이트
	@Override
	@Transactional
	public Short updateSuccessCaseApproval(Integer caseId, Short status) {
		successCaseRepository.updateApprovalStatus(caseId, status);
		return status;
	}

}
