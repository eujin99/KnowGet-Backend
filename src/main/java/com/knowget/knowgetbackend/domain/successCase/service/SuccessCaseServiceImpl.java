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

	// 특정 caseId에 해당하는 SuccessCase 조회 - findById
	@Override
	@Transactional(readOnly = true)
	public SuccessCaseResponseDTO getSuccessCase(Integer caseId) {
		Optional<SuccessCase> successCase = successCaseRepository.findById(caseId);
		return successCase.map(SuccessCaseResponseDTO::new).orElse(null);
		//caseId에 해당하는 successcase를 반환!
	}

	// 전체 SuccessCase 목록 조회 - findAll
	@Override
	@Transactional(readOnly = true)
	public List<SuccessCaseResponseDTO> getAllSuccessCases() {
		return successCaseRepository.findAll().stream().map(SuccessCaseResponseDTO::new).toList();
		//모든 successcase를 반환!
	}

	// SuccessCase 생성 - save
	@Override
	@Transactional
	public SuccessCaseResponseDTO createSuccessCase(SuccessCaseRequestDTO successCaseRequestDTO) {
		// Find the User entity
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

	// SuccessCase 삭제
	@Override
	@Transactional
	public String deleteSuccessCase(Integer caseId) {
		//존재하는지 확인
		if (!successCaseRepository.existsById(caseId)) {
			throw new ResourceNotFoundException("등록된" + caseId + "번 성공사례가 없습니다.");
		}

		//삭제
		successCaseRepository.deleteById(caseId);

		return "해당 글이 삭제 되었습니다";
	}

	// SuccessCase 검색 - By Using "Keyword"
	@Override
	@Transactional(readOnly = true)
	public List<SuccessCaseResponseDTO> searchSuccessCase(String keyword) {
		List<SuccessCase> successCases = successCaseRepository.findByTitleContaining(keyword);
		return successCases.stream().map(SuccessCaseResponseDTO::new).collect(Collectors.toList());
	}

}
