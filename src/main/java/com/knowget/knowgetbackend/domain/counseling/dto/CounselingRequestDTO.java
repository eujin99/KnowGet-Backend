package com.knowget.knowgetbackend.domain.counseling.dto;

import com.knowget.knowgetbackend.global.entity.User;

import lombok.Data;

@Data
public class CounselingRequestDTO {
	private User user;
	private String category;
	private String content;

}
