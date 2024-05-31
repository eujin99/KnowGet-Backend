package com.knowget.knowgetbackend.domain.jobGuide.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class JobGuideResponseDTO {

	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	private Integer guideId;
	private Integer adminId;

	private String username;
	private String title;
	private String content;

	/**
	 * JobGuideResponseDTO 생성자
	 *
	 * @param guideId 게시글 ID
	 * @param adminId 관리자 ID (adminId = 3 으로 만들어 둔 상태)
	 * @param username 작성자 이름
	 * @param title 제목
	 * @param content 내용
	 * @param createdDate 생성 날짜
	 * @param updatedDate 수정 날짜
	 * @author 유진
	 */
	public JobGuideResponseDTO(Integer guideId, Integer adminId, String username, String title, String content,
		LocalDateTime createdDate, LocalDateTime updatedDate) {
		this.guideId = guideId;
		this.adminId = adminId;
		this.username = username;
		this.title = title;
		this.content = content;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}
}
