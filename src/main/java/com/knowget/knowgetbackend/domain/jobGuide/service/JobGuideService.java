package com.knowget.knowgetbackend.domain.jobGuide.service;

import java.util.List;

import com.knowget.knowgetbackend.domain.jobGuide.dto.JobGuideRequestDTO;

public interface JobGuideService {
	List<?> getAllJobGuides(); // 취업가이드 목록 조회

	Object getJobGuideById(Integer id); // 특정 취업가이드 게시글 상세 조회 (게시글 id값으로!!)

	Object createJobGuide(JobGuideRequestDTO jobGuideRequestDTO); // 취업가이드 게시글 생성

	Object updateJobGuide(Integer id, JobGuideRequestDTO jobGuideRequestDTO); // 취업가이드 게시극ㄹ 수정

	void deleteJobGuide(Integer id); // 취업가이드 게시글 삭제
}
