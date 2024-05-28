package com.knowget.knowgetbackend.domain.comment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequestDto {

	// 댓글 내용
	@NotNull
	private String content;
	//유저 아이디
	@NotNull
	private String username;
	// 게시글 번호
	@NotNull
	private Integer caseId;
}
