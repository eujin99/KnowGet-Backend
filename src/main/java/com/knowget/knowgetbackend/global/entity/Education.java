package com.knowget.knowgetbackend.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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

	@Id
	@Column(name = "education_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long educationId;

	@Column(name = "course_nm")
	private String courseNm;

	@Column(name = "course_request_str_dt")
	private String courseRequestStrDt;

	@Column(name = "course_request_end_dt")
	private String courseRequestEndDt;

	@Column(name = "course_str_dt")
	private String courseStrDt;

	@Column(name = "course_end_dt")
	private String courseEndDt;

	@Column(name = "capacity")
	private Integer capacity;

	@Column(name = "course_apply_url", columnDefinition = "TEXT")
	private String courseApplyUrl;

	@Column(name = "dept_nm")
	private String deptNm;

	@Column(name = "gu")
	private String gu;

	@Builder
	public Education(String courseNm, String courseRequestStrDt, String courseRequestEndDt,
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
