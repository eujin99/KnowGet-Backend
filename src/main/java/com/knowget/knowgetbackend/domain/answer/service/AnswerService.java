package com.knowget.knowgetbackend.domain.answer.service;

import com.knowget.knowgetbackend.domain.answer.dto.AnswerModfiyDTO;
import com.knowget.knowgetbackend.domain.answer.dto.AnswerRequestDTO;
import com.knowget.knowgetbackend.domain.answer.dto.AnswerResponseDTO;

public interface AnswerService {
	AnswerResponseDTO getAnswer(Integer id);

	String saveAnswer(AnswerRequestDTO answerRequestDTO);

	String updateAnswer(Integer id, AnswerModfiyDTO answerModfiyDTO);

	String deleteAnswer(Integer id);
}
