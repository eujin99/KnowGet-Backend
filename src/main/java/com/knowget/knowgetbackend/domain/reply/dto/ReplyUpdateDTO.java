package com.knowget.knowgetbackend.domain.reply.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyUpdateDTO {

	private Integer commentId;
	private Integer replyId;
	private String username;
	@NotNull
	private String content;

}
