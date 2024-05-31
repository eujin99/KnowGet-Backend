package com.knowget.knowgetbackend.domain.answer.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knowget.knowgetbackend.domain.admin.repository.AdminRepository;
import com.knowget.knowgetbackend.domain.answer.dto.AnswerModfiyDTO;
import com.knowget.knowgetbackend.domain.answer.dto.AnswerRequestDTO;
import com.knowget.knowgetbackend.domain.answer.dto.AnswerResponseDTO;
import com.knowget.knowgetbackend.domain.answer.exception.AnswerNotFoundException;
import com.knowget.knowgetbackend.domain.answer.repository.AnswerRepository;
import com.knowget.knowgetbackend.domain.counseling.exception.CounselingNotFoundException;
import com.knowget.knowgetbackend.domain.counseling.repository.CounselingRepository;
import com.knowget.knowgetbackend.global.entity.Admin;
import com.knowget.knowgetbackend.global.entity.Answer;
import com.knowget.knowgetbackend.global.entity.Counseling;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

	private final AnswerRepository answerRepository;
	private final AdminRepository adminRepository;
	private final CounselingRepository counselingRepository;

	/** 답변 상세 조회
	 *
	 * @param id
	 * @return AnswerResponseDTO 답변 내용
	 * @author 근엽
	 * @throws AnswerNotFoundException
	 */
	@Transactional(readOnly = true)
	@Override
	public AnswerResponseDTO getAnswer(Integer id) {
		Answer ans = answerRepository.findById(id)
			.orElseThrow(() -> new AnswerNotFoundException("[ERROR] 존재하지않는 답변입니다."));
		return new AnswerResponseDTO(ans);
	}

	/** 답변 작성
	 *
	 * @param answerRequestDTO
	 * @return String 작성 완료 메시지
	 * @author 근엽
	 * @throws CounselingNotFoundException
	 */
	@Transactional
	@Override
	public String saveAnswer(AnswerRequestDTO answerRequestDTO) {
		// String adminId = SecurityContextHolder.getContext().getAuthentication().getName();
		Counseling counseling = counselingRepository.findById(answerRequestDTO.getCounselingId()).orElseThrow(
			() -> new CounselingNotFoundException("[ERROR] 해당 상담을 찾을 수 없습니다."));
		String adminId = "admin";
		Admin admin = adminRepository.findByUsername(adminId);

		Answer ans = Answer.builder()
			.counseling(counseling)
			.admin(admin)
			.content(answerRequestDTO.getContent())
			.build();

		answerRepository.save(ans);
		return "답변이 저장되었습니다.";

	}

	/** 답변 수정
	 *
	 * @param id
	 * @param answerModfiyDTO
	 * @return String 수정 완료 메시지
	 * @author 근엽
	 * @throws AnswerNotFoundException
	 */
	@Transactional
	@Override
	public String updateAnswer(Integer id, AnswerModfiyDTO answerModfiyDTO) {
		Answer answer = answerRepository.findById(id)
			.orElseThrow(() -> new AnswerNotFoundException("[ERROR] 존재하지않는 답변입니다."));
		answer.updateContent(answerModfiyDTO.getContent());
		answerRepository.save(answer);
		return "답변이 수정되었습니다.";

	}

	/** 답변 삭제
	 *
	 * @param id
	 * @return String 삭제 완료 메시지
	 * @author 근엽
	 * @throws AnswerNotFoundException
	 */
	@Transactional
	@Override
	public String deleteAnswer(Integer id) {
		Answer answer = answerRepository.findById(id)
			.orElseThrow(() -> new AnswerNotFoundException("[ERROR] 존재하지않는 답변입니다."));
		answerRepository.delete(answer);
		return "답변이 삭제되었습니다.";
		
	}

}
