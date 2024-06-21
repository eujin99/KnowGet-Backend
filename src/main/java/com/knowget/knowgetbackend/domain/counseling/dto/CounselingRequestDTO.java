package com.knowget.knowgetbackend.domain.counseling.dto;

import lombok.Data;

@Data
public class CounselingRequestDTO {
	private String username;
	private String category;
	private String content;

}
