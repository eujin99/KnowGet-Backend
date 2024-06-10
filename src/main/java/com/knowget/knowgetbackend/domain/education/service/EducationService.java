package com.knowget.knowgetbackend.domain.education.service;

import java.util.List;

import com.knowget.knowgetbackend.global.entity.Education;

public interface EducationService {

	// 1. 모든 교육강의 가져오기
	List<Education> getAllEducations();

	// 2. 교육강의 검색하기
	List<Education> searchEducations(String keyword);

	// 3. 모집중인 교육강의 가져오기
	List<Education> getRecruitingEducations();
}
