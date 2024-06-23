package com.knowget.knowgetbackend.domain.user.dto;

import java.time.LocalDateTime;

import com.knowget.knowgetbackend.global.entity.SuccessCase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WrittenSuccessCaseDTO {

	private String title;
	private String content;
	private Integer isApproved;
	private LocalDateTime createdDate;

	public WrittenSuccessCaseDTO(SuccessCase successCase) {
		this.title = successCase.getTitle();
		this.content = successCase.getContent();
		this.isApproved = successCase.getIsApproved();
		this.createdDate = successCase.getCreatedDate();
	}

}
