package com.knowget.knowgetbackend.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "education")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Education {

	// 강의명
	@Id
	@Column(name = "course_nm")
	private String courseNm;

	// 수강신청 시작 일자
	@Column(name = "course_request_str_dt")
	private String courseRequestStrDt;

	// 수강신청 종료 일자
	@Column(name = "course_request_end_dt")
	private String courseRequestEndDt;

	// 강의 시작 일자
	@Column(name = "course_str_dt")
	private String courseStrDt;

	// 강의 종료 일자
	@Column(name = "course_end_dt")
	private String courseEndDt;

	// 정원
	@Column(name = "capacity")
	private Integer capacity;

	// 수강신청 URL
	@Column(name = "course_apply_url")
	private String courseApplyUrl;

	// 교육기관명
	@Column(name = "dept_nm")
	private String deptNm;

	// 교육기관 자치구
	@Column(name = "gu")
	private String gu;

	@Builder
	public Education(String courseNm, String courseRequestStrDt, String courseRequestEndDt, String courseStrDt,
		String courseEndDt, Integer capacity, String courseApplyUrl, String deptNm, String gu) {
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
