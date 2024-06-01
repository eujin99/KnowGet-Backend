package com.knowget.knowgetbackend.domain.reply.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReplyDeleteDTO {

	private Integer commentId;
	private Integer replyId;
	private String username;

	@Builder
	public ReplyDeleteDTO(Integer commentId, Integer replyId, String username) {
		this.commentId = commentId;
		this.replyId = replyId;
		this.username = username;
	}

}
