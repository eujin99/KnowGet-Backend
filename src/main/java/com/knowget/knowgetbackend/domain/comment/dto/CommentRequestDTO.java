package com.knowget.knowgetbackend.domain.comment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDTO {

	private Integer caseId;
	private String username;
	@NotNull
	private String content;

}
