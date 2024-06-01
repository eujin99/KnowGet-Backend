package com.knowget.knowgetbackend.domain.reply.dto;

import java.time.LocalDateTime;

import com.knowget.knowgetbackend.global.entity.Reply;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyResponseDTO {

	private Integer replyId;
	private Integer commentId;
	private String username;
	private String content;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;

	public ReplyResponseDTO(Reply reply) {
		this.replyId = reply.getReplyId();
		this.commentId = reply.getComment().getCommentId();
		this.username = reply.getUser().getUsername();
		this.content = reply.getContent();
		this.createdDate = reply.getCreatedDate();
		this.updatedDate = reply.getUpdatedDate();
	}

}
