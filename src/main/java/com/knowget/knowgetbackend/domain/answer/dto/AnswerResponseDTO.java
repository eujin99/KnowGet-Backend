package com.knowget.knowgetbackend.domain.answer.dto;

import com.knowget.knowgetbackend.global.entity.Answer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResponseDTO {

	private Integer answerId;

	private String counseling;

	private String content;

	public AnswerResponseDTO(Answer answer) {
		this.answerId = answer.getAnswerId();
		this.counseling = answer.getCounseling().getContent();
		this.content = answer.getContent();
	}

}
