package com.knowget.knowgetbackend.domain.answer.dto;

import com.knowget.knowgetbackend.global.entity.Admin;
import com.knowget.knowgetbackend.global.entity.Answer;
import com.knowget.knowgetbackend.global.entity.Counseling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResponseDTO {

	private Integer answerId;

	private Counseling counseling;

	private Admin admin;

	private String content;

	public AnswerResponseDTO(Answer answer) {
		this.answerId = answer.getAnswerId();
		this.counseling = answer.getCounseling();
		this.admin = answer.getAdmin();
		this.content = answer.getContent();
	}

}
