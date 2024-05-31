package com.knowget.knowgetbackend.domain.successCase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knowget.knowgetbackend.domain.successCase.dto.SuccessCaseRequestDTO;
import com.knowget.knowgetbackend.domain.successCase.dto.SuccessCaseResponseDTO;
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
	// postman 사용법 : get으로 요청 route : http://localhost:8080/api/v1/success-case/{caseId}
	@Override
	@Transactional(readOnly = true)
	public SuccessCaseResponseDTO getSuccessCase(Integer caseId) {
		Optional<SuccessCase> successCase = successCaseRepository.findById(caseId);
		return successCase.map(SuccessCaseResponseDTO::new).orElse(null);
		//caseId에 해당하는 successcase를 반환!
	}

	// 전체 SuccessCase 목록 조회 - findAll
	// postman 사용법 : get으로 요청 route : http://localhost:8080/api/v1/success-case
	@Override
	@Transactional(readOnly = true)
	public List<SuccessCaseResponseDTO> getAllSuccessCases() {
		return successCaseRepository.findAll().stream().map(SuccessCaseResponseDTO::new).toList();
		//모든 successcase를 반환!
	}

	// SuccessCase 생성 - save
	// postman 사용법 : username / title / content 입력 후 post로 요청
	// 성공 시 200 OK 반환
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

		// Save the SuccessCase
		successCaseRepository.save(successCase);

		// Convert the saved SuccessCase to SuccessCaseResponseDTO and return it
		return new SuccessCaseResponseDTO(successCase);
	}
}
