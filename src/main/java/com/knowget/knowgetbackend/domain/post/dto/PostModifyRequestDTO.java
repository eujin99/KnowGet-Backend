package com.knowget.knowgetbackend.domain.post.dto;

import lombok.Data;

@Data
public class PostModifyRequestDTO {
	private String title;
	private String content;
	private String id;
}
