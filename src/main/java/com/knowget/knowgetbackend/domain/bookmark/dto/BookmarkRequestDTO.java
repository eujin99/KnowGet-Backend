package com.knowget.knowgetbackend.domain.bookmark.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookmarkRequestDTO {

	private String joRegistNo;
	private String username;

	@Builder
	public BookmarkRequestDTO(String joRegistNo, String username) {
		this.joRegistNo = joRegistNo;
		this.username = username;
	}

}
