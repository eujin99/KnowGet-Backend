package com.knowget.knowgetbackend.domain.counseling.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knowget.knowgetbackend.domain.counseling.dto.CounselingRequestDTO;
import com.knowget.knowgetbackend.domain.counseling.dto.CounselingResponseDTO;
import com.knowget.knowgetbackend.domain.counseling.repository.CounselingRepository;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.Counseling;
import com.knowget.knowgetbackend.global.exception.CounselingNotFoundException;
import com.knowget.knowgetbackend.global.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CounselingServiceImpl implements CounselingService {

	private final CounselingRepository counselingRepository;
	private final UserRepository userRepository;

	/**
	 * 최신 순으로 상담 목록 조회 (관리자 입장에서 모든 상담 조회)
	 *
	 * @return List<CounselingResponseDTO> 상담 목록
	 * @author 근엽
	 */
	@Override
	@Transactional(readOnly = true)
	public List<CounselingResponseDTO> getAllCounseling() {

		return counselingRepository.findAllByOrderBySentDateDesc().stream()
			.map(CounselingResponseDTO::new)
			.collect(Collectors.toList());
	}

	/**
	 * 상담 상세 조회
	 *
	 * @param id
	 * @return CounselingResponseDTO 상담 내용
	 * @throws CounselingNotFoundException
	 * @author 근엽
	 */
	@Override
	@Transactional(readOnly = true)
	public CounselingResponseDTO getCounselingById(Integer id) {

		Counseling counseling = counselingRepository.findById(id)
			.orElseThrow(() -> new CounselingNotFoundException("[Error] 해당 상담을 찾을 수 없습니다."));
		return new CounselingResponseDTO(counseling);
	}

	/**
	 * 상담 작성
	 *
	 * @param counselingRequestDTO
	 * @return String 작성 완료 메시지
	 * @throws UserNotFoundException
	 * @author 근엽
	 */
	@Override
	@Transactional
	public String saveCounseling(CounselingRequestDTO counselingRequestDTO) {

		Counseling counseling = Counseling.builder()
			.user(userRepository.findByUsername(counselingRequestDTO.getUsername())
				.orElseThrow(() -> new UserNotFoundException("[Error] 해당 사용자를 찾을 수 없습니다.")))
			.category(counselingRequestDTO.getCategory())
			.content(counselingRequestDTO.getContent())
			.build();
		counselingRepository.save(counseling);
		return "상담이 저장되었습니다.";
	}

}


