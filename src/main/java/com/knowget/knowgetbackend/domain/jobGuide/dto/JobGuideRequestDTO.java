package com.knowget.knowgetbackend.domain.jobGuide.dto;

import lombok.Data;

@Data
public class JobGuideRequestDTO {
	private Integer adminId; // 관리자 ID
	private String title; // 취업가이드 제목
	private String content; // 취업가이드 내용
}
