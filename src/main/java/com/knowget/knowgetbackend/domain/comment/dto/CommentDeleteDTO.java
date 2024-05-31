package com.knowget.knowgetbackend.domain.comment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentDeleteDTO {

	private Integer caseId;
	private Integer commentId;
	private String username;

	@Builder
	public CommentDeleteDTO(Integer caseId, Integer commentId, String username) {
		this.caseId = caseId;
		this.commentId = commentId;
		this.username = username;
	}

}
