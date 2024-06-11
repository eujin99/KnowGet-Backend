package com.knowget.knowgetbackend.domain.education.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EducationResponseDTO {
	// 강의 명
	private String courseNm;
	// 수강신청 시작 일자
	private String courseRequestStrDt;
	// 수강신청 종료 일자
	private String courseRequestEndDt;
	// 강의 시작 일자
	private String courseStrDt;
	// 강의 종료 일자
	private String courseEndDt;
	// 정원
	private Integer capacity;
	// 수강신청 URL
	private String courseApplyUrl;
	// 교육기관명
	private String deptNm;
	// 교육기관 자치구
	private String gu;

	@Builder
	public EducationResponseDTO(String courseNm, String courseRequestStrDt, String courseRequestEndDt,
		String courseStrDt, String courseEndDt, Integer capacity, String courseApplyUrl, String deptNm, String gu) {
		this.courseNm = courseNm;
		this.courseRequestStrDt = courseRequestStrDt;
		this.courseRequestEndDt = courseRequestEndDt;
		this.courseStrDt = courseStrDt;
		this.courseEndDt = courseEndDt;
		this.capacity = capacity;
		this.courseApplyUrl = courseApplyUrl;
		this.deptNm = deptNm;
		this.gu = gu;
	}

}
