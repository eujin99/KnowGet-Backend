package com.knowget.knowgetbackend.domain.successCase.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knowget.knowgetbackend.domain.successCase.dto.SuccessCaseRequestDTO;
import com.knowget.knowgetbackend.domain.successCase.dto.SuccessCaseResponseDTO;
import com.knowget.knowgetbackend.domain.successCase.repository.SuccessCaseRepository;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.dto.ResultMessageDTO;
import com.knowget.knowgetbackend.global.entity.SuccessCase;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.exception.RequestFailedException;
import com.knowget.knowgetbackend.global.exception.ResourceNotFoundException;
import com.knowget.knowgetbackend.global.exception.SuccessCaseNotFoundException;
import com.knowget.knowgetbackend.global.exception.UserNotFoundException;

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
		try {
			SuccessCase successCase = successCaseRepository.findById(caseId)
				.orElseThrow(() -> new SuccessCaseNotFoundException(caseId + "번 게시글을 찾을 수 없습니다"));
			return new SuccessCaseResponseDTO(successCase);
		} catch (Exception e) {
			throw new RequestFailedException("[Error] : " + e.getMessage());
		}
	}

	/**
	 * 2. 전체 SuccessCase 목록 조회
	 *
	 * @return 전체 취업 성공사례 목록 리스트
	 * @author MJ
	 */

	// 2. 전체 SuccessCase 목록 조회 - findAll
	@Override
	@Transactional(readOnly = true)
	public List<SuccessCaseResponseDTO> getAllSuccessCases() {
		List<SuccessCase> successCases = successCaseRepository.findAll();
		if (successCases.isEmpty()) {
			throw new ResourceNotFoundException("[Error] : 등록된 게시글이 없습니다");
		}
		return successCases.stream().map(SuccessCaseResponseDTO::new).toList();
	}

	/**
	 * 3. SuccessCase 생성
	 *
	 * @param successCaseRequestDTO username : 사용자 ID, title : 게시글 제목, content : 게시글 내용
	 * @return SuccessCaseResponseDTO
	 * @throws UserNotFoundException 존재하지 않는 게시글일 경우
	 * @author MJ
	 */

	// 3. SuccessCase 생성
	@Override
	@Transactional
	public ResultMessageDTO createSuccessCase(SuccessCaseRequestDTO successCaseRequestDTO) {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();

			// 사용자 조회 및 예외 처리
			User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다"));

			// SuccessCase 생성
			SuccessCase successCase = SuccessCase.builder()
				.title(successCaseRequestDTO.getTitle())
				.content(successCaseRequestDTO.getContent())
				.user(user)
				.build();

			// SuccessCase 저장
			successCaseRepository.save(successCase);

			// 응답 DTO 생성 및 반환
			return new ResultMessageDTO("SuccessCase가 성공적으로 생성되었습니다");
		} catch (Exception e) {
			throw new RequestFailedException("[Error] : " + e.getMessage());
		}
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
	public ResultMessageDTO deleteSuccessCase(Integer caseId) {
		// 존재하는지 확인
		try {
			SuccessCase successCase = successCaseRepository.findById(caseId)
				.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 게시글입니다"));
			successCaseRepository.delete(successCase);
			return new ResultMessageDTO("해당 글이 삭제 되었습니다");
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 삭제에 실패했습니다 : " + e.getMessage());
		}
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
		if (successCases.isEmpty()) {
			throw new ResourceNotFoundException("[Error] : 해당 검색어와 일치하는 제목 게시글이 없습니다");
		}
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
	public String updateSuccessCaseApproval(Integer caseId, Integer status) {
		SuccessCase successCase = successCaseRepository.findById(caseId)
			.orElseThrow(() -> new SuccessCaseNotFoundException("[Error] : 해당 게시글이 존재하지 않습니다"));
		String result = switch (status) {
			case 1 -> "승인되었습니다";
			case 2 -> "거절되었습니다";
			default -> "승인 대기 상태로 변경되었습니다";
		};
		successCase.updateIsApproved(status);
		successCaseRepository.save(successCase);
		// successCaseRepository.updateApprovalStatus(caseId, status);
		return caseId + "번 취업 성공사례 게시가 " + result;
	}

}
