package com.knowget.knowgetbackend.domain.jobGuide.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knowget.knowgetbackend.domain.admin.repository.AdminRepository;
import com.knowget.knowgetbackend.domain.jobGuide.dto.JobGuideRequestDTO;
import com.knowget.knowgetbackend.domain.jobGuide.repository.JobGuideRepository;
import com.knowget.knowgetbackend.global.entity.Admin;
import com.knowget.knowgetbackend.global.entity.JobGuide;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobGuideServiceImpl implements JobGuideService {

	private final JobGuideRepository jobGuideRepository;
	private final AdminRepository adminRepository;
	// AdminRepository 있다고 치고!!
	// 나중에 만들어줄거다요.
	// 일단 admin 계정 admin으로 만들어뒀어욥. adminId = 3 입니다 !! (게시글 등록 때 사용)

	/**
	 * 취업가이드 게시글 목록 조회
	 * @return 취업가이드 게시글 목록이욤
	 * @author 유진
	 */
	@Override
	@Transactional(readOnly = true)
	public List<JobGuide> getAllJobGuides() {
		return jobGuideRepository.findAll();
	}

	/**
	 * 취업가이드 게시글 상세 조회
	 * @param id 조회할 게시글 id
	 * @return 조회된 게시글
	 * @author 유진
	 */
	@Override
	@Transactional(readOnly = true)
	public JobGuide getJobGuideById(Integer id) {
		return jobGuideRepository.findById(id).orElse(null);
	}

	/**
	 * 새로운 취업가이드 겍시글 생성
	 * @param jobGuideRequestDTO 생성할 게시글 정보
	 * @return 생성됭 게시글
	 * @author 유진
	 */

	@Override
	@Transactional
	public JobGuide createJobGuide(JobGuideRequestDTO jobGuideRequestDTO) {
		Admin admin = adminRepository.findById(jobGuideRequestDTO.getAdminId()).orElse(null);

		JobGuide jobGuide = JobGuide.builder()
			.admin(admin)
			.title(jobGuideRequestDTO.getTitle())
			.content(jobGuideRequestDTO.getContent())
			.build();

		return jobGuideRepository.save(jobGuide);
	}

	/**
	 * 취업가이드 게시글 수정
	 * @param id 수정할 게시글 id
	 * @param jobGuideRequestDTO 수정할 게시글 정보
	 * @return 수정된 게시글
	 * @author 유진
	 */

	@Override
	@Transactional
	public JobGuide updateJobGuide(Integer id, JobGuideRequestDTO jobGuideRequestDTO) {
		JobGuide jobGuide = jobGuideRepository.findById(id).orElse(null);

		if (jobGuide != null) {
			jobGuide.updateTitle(jobGuideRequestDTO.getTitle());
			jobGuide.updateContent(jobGuideRequestDTO.getContent());
		}

		return jobGuide;
	}

	/**
	 * 취업가이드 게시글 삭제
	 * @param id 삭제할 게시글 id
	 * @author 유진
	 */
	@Override
	@Transactional
	public void deleteJobGuide(Integer id) {
		if (jobGuideRepository.existsById(id)) {
			jobGuideRepository.deleteById(id);
		}
	}
}
