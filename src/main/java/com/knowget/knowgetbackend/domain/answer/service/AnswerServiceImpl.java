package com.knowget.knowgetbackend.domain.answer.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knowget.knowgetbackend.domain.answer.dto.AnswerModfiyDTO;
import com.knowget.knowgetbackend.domain.answer.dto.AnswerRequestDTO;
import com.knowget.knowgetbackend.domain.answer.dto.AnswerResponseDTO;
import com.knowget.knowgetbackend.domain.answer.repository.AnswerRepository;
import com.knowget.knowgetbackend.domain.counseling.repository.CounselingRepository;
import com.knowget.knowgetbackend.global.entity.Answer;
import com.knowget.knowgetbackend.global.entity.Counseling;
import com.knowget.knowgetbackend.global.exception.AnswerNotFoundException;
import com.knowget.knowgetbackend.global.exception.CounselingNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

	private final AnswerRepository answerRepository;
	private final CounselingRepository counselingRepository;

	/**
	 * 답변 상세 조회
	 *
	 * @param id
	 * @return AnswerResponseDTO 답변 내용
	 * @throws AnswerNotFoundException
	 * @author 근엽
	 */
	@Transactional(readOnly = true)
	@Override
	public AnswerResponseDTO getAnswer(Integer id) {
		Answer ans = answerRepository.findById(id)
			.orElseThrow(() -> new AnswerNotFoundException("[Error] 존재하지않는 답변입니다."));
		return new AnswerResponseDTO(ans);
	}

	/**
	 * 답변 작성
	 *
	 * @param answerRequestDTO
	 * @return String 작성 완료 메시지
	 * @throws CounselingNotFoundException
	 * @author 근엽
	 */
	@Transactional
	@Override
	public String saveAnswer(AnswerRequestDTO answerRequestDTO) {
		Counseling counseling = counselingRepository.findById(answerRequestDTO.getCounselingId()).orElseThrow(
			() -> new CounselingNotFoundException("[Error] 해당 상담을 찾을 수 없습니다."));
		if (counseling.getIsAnswered()) {
			throw new IllegalArgumentException("[Error] 이미 답변이 존재합니다.");
		}

		counseling.updateIsAnswered(true);
		counselingRepository.save(counseling);

		Answer ans = Answer.builder()
			.counseling(counseling)
			.content(answerRequestDTO.getContent())
			.build();

		answerRepository.save(ans);
		return "답변이 저장되었습니다.";

	}

	/**
	 * 답변 수정
	 *
	 * @param id
	 * @param answerModfiyDTO
	 * @return String 수정 완료 메시지
	 * @throws AnswerNotFoundException
	 * @author 근엽
	 */
	@Transactional
	@Override
	public String updateAnswer(Integer id, AnswerModfiyDTO answerModfiyDTO) {
		Answer answer = answerRepository.findById(id)
			.orElseThrow(() -> new AnswerNotFoundException("[Error] 존재하지않는 답변입니다."));
		answer.updateContent(answerModfiyDTO.getContent());
		answerRepository.save(answer);
		return "답변이 수정되었습니다.";

	}

	/**
	 * 답변 삭제
	 *
	 * @param id 답변 ID
	 * @return String 삭제 완료 메시지
	 * @throws AnswerNotFoundException 답변이 존재하지 않을 때 발생하는 예외
	 * @author 근엽
	 */
	@Transactional
	@Override
	public String deleteAnswer(Integer id) {
		Answer answer = answerRepository.findById(id)
			.orElseThrow(() -> new AnswerNotFoundException("[Error] 존재하지않는 답변입니다."));

		answerRepository.delete(answer);
		return "답변이 삭제되었습니다.";

	}

}
