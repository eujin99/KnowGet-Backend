package com.knowget.knowgetbackend.domain.jobGuide.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.knowget.knowgetbackend.domain.admin.repository.AdminRepository;
import com.knowget.knowgetbackend.domain.jobGuide.dto.JobGuideRequestDTO;
import com.knowget.knowgetbackend.domain.jobGuide.dto.JobGuideResponseDTO;
import com.knowget.knowgetbackend.domain.jobGuide.repository.JobGuideRepository;
import com.knowget.knowgetbackend.global.entity.Admin;
import com.knowget.knowgetbackend.global.entity.JobGuide;
import com.knowget.knowgetbackend.global.exception.ResourceNotFoundException;

class JobGuideServiceImplTest {
	@Mock
	private JobGuideRepository jobGuideRepository;

	@Mock
	private AdminRepository adminRepository;

	@InjectMocks
	private JobGuideServiceImpl jobGuideService;

	private JobGuide jobGuide;
	private Admin admin;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		admin = Admin.builder()
			.username("admin")
			.build();

		jobGuide = JobGuide.builder()
			.title("Sample Title")
			.content("Sample Content")
			.admin(admin)
			.build();
	}

	@Test
	@DisplayName("모든 취업가이드 게시글 목록 조회 테스트 - getAllJobGuides")
	void testGetAllJobGuides() {
		when(jobGuideRepository.findAll()).thenReturn(List.of(jobGuide));

		List<JobGuideResponseDTO> result = jobGuideService.getAllJobGuides();

		assertThat(result).hasSize(1);
		verify(jobGuideRepository, times(1)).findAll();
	}

	@Test
	@DisplayName("취업가이드 게시글 상세 조회 테스트 - getJobGuideById")
	void testGetJobGuideById() {
		when(jobGuideRepository.findById(1)).thenReturn(Optional.of(jobGuide));

		JobGuideResponseDTO result = jobGuideService.getJobGuideById(1);

		assertThat(result.getTitle()).isEqualTo("Sample Title");
		verify(jobGuideRepository, times(1)).findById(1);
	}

	@Test
	@DisplayName("취업가이드 게시글 상세 조회 실패 테스트 - 존재하지 않는 게시글")
	void testGetJobGuideByIdNotFound() {
		when(jobGuideRepository.findById(1)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> jobGuideService.getJobGuideById(1));
		verify(jobGuideRepository, times(1)).findById(1);
	}

	@Test
	@DisplayName("새로운 취업가이드 게시글 생성 테스트 - createJobGuide")
	void testCreateJobGuide() {
		JobGuideRequestDTO requestDTO = new JobGuideRequestDTO();
		requestDTO.setUsername("admin");
		requestDTO.setTitle("New Title");
		requestDTO.setContent("New Content");

		when(adminRepository.findByUsername("admin")).thenReturn(admin);
		when(jobGuideRepository.save(any(JobGuide.class))).thenReturn(jobGuide);

		JobGuideResponseDTO result = jobGuideService.createJobGuide(requestDTO);

		assertThat(result.getTitle()).isEqualTo("New Title");
		verify(adminRepository, times(1)).findByUsername("admin");
		verify(jobGuideRepository, times(1)).save(any(JobGuide.class));
	}

	@Test
	@DisplayName("취업가이드 게시글 수정 테스트 - updateJobGuide")
	void testUpdateJobGuide() {
		JobGuideRequestDTO requestDTO = new JobGuideRequestDTO();
		requestDTO.setTitle("Updated Title");
		requestDTO.setContent("Updated Content");

		when(jobGuideRepository.findById(1)).thenReturn(Optional.of(jobGuide));
		when(jobGuideRepository.save(any(JobGuide.class))).thenReturn(jobGuide);

		JobGuideResponseDTO result = jobGuideService.updateJobGuide(1, requestDTO);

		assertThat(result.getTitle()).isEqualTo("Updated Title");
		verify(jobGuideRepository, times(1)).findById(1);
		verify(jobGuideRepository, times(1)).save(any(JobGuide.class));
	}

	@Test
	@DisplayName("취업가이드 게시글 수정 실패 테스트 - 존재하지 않는 게시글")
	void testUpdateJobGuideNotFound() {
		JobGuideRequestDTO requestDTO = new JobGuideRequestDTO();
		requestDTO.setTitle("Updated Title");
		requestDTO.setContent("Updated Content");

		when(jobGuideRepository.findById(1)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> jobGuideService.updateJobGuide(1, requestDTO));
		verify(jobGuideRepository, times(1)).findById(1);
	}

	@Test
	@DisplayName("취업가이드 게시글 삭제 테스트 - deleteJobGuide")
	void testDeleteJobGuide() {
		when(jobGuideRepository.existsById(1)).thenReturn(true);

		jobGuideService.deleteJobGuide(1);

		verify(jobGuideRepository, times(1)).existsById(1);
		verify(jobGuideRepository, times(1)).deleteById(1);
	}

	@Test
	@DisplayName("취업가이드 게시글 삭제 실패 테스트 - 존재하지 않는 게시글")
	void testDeleteJobGuideNotFound() {
		when(jobGuideRepository.existsById(1)).thenReturn(false);

		assertThrows(ResourceNotFoundException.class, () -> jobGuideService.deleteJobGuide(1));
		verify(jobGuideRepository, times(1)).existsById(1);
	}
}