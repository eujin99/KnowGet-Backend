package com.knowget.knowgetbackend.domain.jobGuide.dto;

import lombok.Data;

@Data
public class JobGuideRequestDTO {
	private Integer adminId; // 관리자 ID (일단 adminId = 3 으로 추가해둠)
	private String username; // 작성자 이름
	private String title; // 취업가이드 제목
	private String content; // 취업가이드 내용
}
