package com.knowget.knowgetbackend.domain.bookmark.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookmarkRequestDTO {

	private Integer postId;
	private String username;

	@Builder
	public BookmarkRequestDTO(Integer postId, String username) {
		this.postId = postId;
		this.username = username;
	}

}
