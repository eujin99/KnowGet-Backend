package com.knowget.knowgetbackend.domain.successCase.dto;

import java.time.LocalDateTime;

import com.knowget.knowgetbackend.global.entity.SuccessCase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessCaseResponseDTO {

	private Integer caseId;
	private String username;
	private String title;
	private String content;
	private Integer isApproved;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;

	public SuccessCaseResponseDTO(SuccessCase successCase) {
		this.caseId = successCase.getCaseId();
		this.username = successCase.getUser().getUsername();
		this.title = successCase.getTitle();
		this.content = successCase.getContent();
		this.isApproved = successCase.getIsApproved();
		this.createdDate = successCase.getCreatedDate();
		this.updatedDate = successCase.getUpdatedDate();
	}

}