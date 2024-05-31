package com.knowget.knowgetbackend.domain.answer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.knowget.knowgetbackend.domain.answer.dto.AnswerModfiyDTO;
import com.knowget.knowgetbackend.domain.answer.dto.AnswerRequestDTO;
import com.knowget.knowgetbackend.domain.answer.dto.AnswerResponseDTO;
import com.knowget.knowgetbackend.domain.answer.service.AnswerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/answer")
public class AnswerController {

	private final AnswerService answerService;

	/** 답변 상세 조회
	 *
	 * @param id
	 * @return ResponseEntity<AnswerResponseDTO> 답변 내용
	 * @author 근엽
	 */
	@PostMapping("/{id}")
	public ResponseEntity<AnswerResponseDTO> getAnswer(@PathVariable Integer id) {
		AnswerResponseDTO answer = answerService.getAnswer(id);

		return new ResponseEntity<>(answer, HttpStatus.OK);
	}

	/** 답변 작성
	 *
	 * @param answerRequestDTO
	 * @return ResponseEntity<String> 작성 완료 메시지
	 * @author 근엽
	 */
	@PostMapping("/")
	public ResponseEntity<String> saveAnswer(@RequestBody AnswerRequestDTO answerRequestDTO) {

		String answer = answerService.saveAnswer(answerRequestDTO);

		return new ResponseEntity<>(answer, HttpStatus.OK);
	}

	/** 답변 수정
	 *
	 * @param id
	 * @param answerModfiyDTO
	 * @return ResponseEntity<String> 수정 완료 메시지
	 * @author 근엽
	 */
	@PatchMapping("/{id}")
	public ResponseEntity<String> updateAnswer(@PathVariable Integer id, @RequestBody AnswerModfiyDTO answerModfiyDTO) {
		String updateAnswer = answerService.updateAnswer(id, answerModfiyDTO);

		return new ResponseEntity<>(updateAnswer, HttpStatus.OK);
	}

	/** 답변 삭제
	 *
	 * @param id
	 * @return ResponseEntity<String> 삭제 완료 메시지
	 * @author 근엽
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteAnswer(@PathVariable Integer id) {
		String deleteAnswer = answerService.deleteAnswer(id);

		return new ResponseEntity<>(deleteAnswer, HttpStatus.OK);
	}

}
