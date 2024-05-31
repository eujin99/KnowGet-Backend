package com.knowget.knowgetbackend.domain.comment.dto;

import java.time.LocalDateTime;

import com.knowget.knowgetbackend.global.entity.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {

	private Integer commentId;
	private Integer caseId;
	private String username;
	private String content;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;

	public CommentResponseDTO(Comment comment) {
		this.commentId = comment.getCommentId();
		this.caseId = comment.getSuccessCase().getCaseId();
		this.username = comment.getUser().getUsername();
		this.content = comment.getContent();
		this.createdDate = comment.getCreatedDate();
		this.updatedDate = comment.getUpdatedDate();
	}
	
}
