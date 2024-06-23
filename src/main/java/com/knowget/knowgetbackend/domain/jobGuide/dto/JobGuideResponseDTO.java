package com.knowget.knowgetbackend.domain.jobGuide.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class JobGuideResponseDTO {

	private Integer guideId;
	private String username;
	private String title;
	private String content;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;

	/**
	 * JobGuideResponseDTO 생성자
	 *
	 * @param guideId     게시글 ID
	 * @param username    작성자 계정명 (관리자)
	 * @param title       제목
	 * @param content     내용
	 * @param createdDate 생성 날짜
	 * @param updatedDate 수정 날짜
	 * @author 유진
	 */
	public JobGuideResponseDTO(Integer guideId, String username, String title, String content,
		LocalDateTime createdDate, LocalDateTime updatedDate) {
		this.guideId = guideId;
		this.username = username;
		this.title = title;
		this.content = content;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}
}
