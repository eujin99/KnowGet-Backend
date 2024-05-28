package com.knowget.knowgetbackend.domain.comment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentUpdateDto {
	@NotNull
	private String username;
	@NotNull
	private String content;
}
