package com.knowget.knowgetbackend.domain.jobGuide.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knowget.knowgetbackend.domain.admin.repository.AdminRepository;
import com.knowget.knowgetbackend.domain.jobGuide.dto.JobGuideRequestDTO;
import com.knowget.knowgetbackend.domain.jobGuide.dto.JobGuideResponseDTO;
import com.knowget.knowgetbackend.domain.jobGuide.repository.JobGuideRepository;
import com.knowget.knowgetbackend.global.entity.Admin;
import com.knowget.knowgetbackend.global.entity.JobGuide;
import com.knowget.knowgetbackend.global.exception.ResourceNotFoundException;

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
	 *
	 * @return 취업가이드 게시글 목록이욤
	 * @author 유진
	 */
	@Override
	@Transactional(readOnly = true)
	public List<JobGuideResponseDTO> getAllJobGuides() {
		return jobGuideRepository.findAll().stream()
			.map(this::convertToDTO)
			.collect(Collectors.toList());
	}

	/**
	 * 취업가이드 게시글 상세 조회
	 *
	 * @param id 조회할 게시글 id (얘로 특정 게시글 조회 가능함!)
	 * @return 조회된 게시글
	 * @author 유진
	 */

	@Override
	@Transactional(readOnly = true)
	public JobGuideResponseDTO getJobGuideById(Integer id) {
		JobGuide jobGuide = jobGuideRepository.findById(id).orElse(null);
		if (jobGuide == null) {
			throw new ResourceNotFoundException("[Error] : 등록된" + id + "번 취업가이드가 없습니다. ");
		}
		return convertToDTO(jobGuide);
	}

	/**
	 * 새로운 취업가이드 겍시글 생성
	 *
	 * @param jobGuideRequestDTO 생성할 게시글 정보
	 * @return 생성됭 게시글
	 * @author 유진
	 */

	@Override
	@Transactional
	public JobGuideResponseDTO createJobGuide(JobGuideRequestDTO jobGuideRequestDTO) {
		Admin admin = adminRepository.findByUsername(jobGuideRequestDTO.getUsername());

		JobGuide jobGuide = JobGuide.builder()
			.admin(admin)
			.title(jobGuideRequestDTO.getTitle())
			.content(jobGuideRequestDTO.getContent())
			.build();

		jobGuideRepository.save(jobGuide);
		return convertToDTO(jobGuide);
	}

	/**
	 * 취업가이드 게시글 수정
	 *
	 * @param id                 수정할 게시글 id
	 * @param jobGuideRequestDTO 수정할 게시글 정보
	 * @return 수정된 게시글
	 * @author 유진
	 */

	@Override
	@Transactional
	public JobGuideResponseDTO updateJobGuide(Integer id, JobGuideRequestDTO jobGuideRequestDTO) {
		JobGuide jobGuide = jobGuideRepository.findById(id).orElse(null);

		if (jobGuide == null) {
			throw new ResourceNotFoundException("[Error] : 등록된" + id + "번 취업가이드가 없습니다. 그러니 수정할 수 없겠죠?");
		}

		jobGuide.updateTitle(jobGuideRequestDTO.getTitle());
		jobGuide.updateContent(jobGuideRequestDTO.getContent());
		jobGuideRepository.save(jobGuide);

		return convertToDTO(jobGuide);
	}

	/**
	 * 취업가이드 게시글 삭제
	 *
	 * @param id 삭제할 게시글 id
	 * @author 유진
	 */
	@Override
	@Transactional
	public void deleteJobGuide(Integer id) {
		if (!jobGuideRepository.existsById(id)) {
			throw new ResourceNotFoundException(
				"[Error] : 등록된" + id + "번 취업가이드가 애초에 없습니다. 삭제할 것이 없어요. 있었는데 ? 아뇨 그냥 없어요.");
		}
		jobGuideRepository.deleteById(id);
	}

	/**
	 * JobGuide Entity -> JobGuideResponseDTO 로 변환!
	 *
	 * @param jobGuide 변환할 JobGuide Entity
	 * @return JobGuide 엔티티 데이터를 포함하는 JobGuideResponseDTO
	 * @author 유진
	 */
	private JobGuideResponseDTO convertToDTO(JobGuide jobGuide) {
		return new JobGuideResponseDTO(
			jobGuide.getGuideId(),
			jobGuide.getTitle(),
			jobGuide.getContent(),
			jobGuide.getCreatedDate(),
			jobGuide.getUpdatedDate()
		);
	}
}
