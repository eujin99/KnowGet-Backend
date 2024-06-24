package com.knowget.knowgetbackend.domain.jobGuide.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.knowget.knowgetbackend.domain.jobGuide.dto.JobGuideRequestDTO;
import com.knowget.knowgetbackend.domain.jobGuide.dto.JobGuideResponseDTO;
import com.knowget.knowgetbackend.domain.jobGuide.repository.JobGuideRepository;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.JobGuide;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.exception.RequestFailedException;
import com.knowget.knowgetbackend.global.exception.ResourceNotFoundException;

class JobGuideServiceImplTest {
	@Mock
	private JobGuideRepository jobGuideRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private JobGuideServiceImpl jobGuideService;

	private User user;
	private JobGuide jobGuide;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);

		user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.role("ADMIN")
			.build();

		jobGuide = JobGuide.builder()
			.user(user)
			.title("Job Guide 1")
			.content("Content for Job Guide 1")
			.build();

	}

	@Test
	@DisplayName("모든 취업가이드 조회 테스트")
	public void testGetAllJobGuides() {
		// Given
		when(jobGuideRepository.findAll()).thenReturn(Arrays.asList(jobGuide));

		// When
		List<JobGuideResponseDTO> result = jobGuideService.getAllJobGuides();

		// Then
		assertThat(result).hasSize(1);
		assertThat(result.get(0).getTitle()).isEqualTo("Job Guide 1");
		assertThat(result.get(0).getContent()).isEqualTo("Content for Job Guide 1");
	}

	@Test
	@DisplayName("취업가이드 상세 조회 테스트")
	public void testGetJobGuideById() {
		// Given
		when(jobGuideRepository.findById(anyInt())).thenReturn(Optional.of(jobGuide));

		// When
		JobGuideResponseDTO result = jobGuideService.getJobGuideById(1);

		// Then
		assertThat(result.getTitle()).isEqualTo("Job Guide 1");
		assertThat(result.getContent()).isEqualTo("Content for Job Guide 1");
	}

	@Test
	@DisplayName("취업가이드 상세 조회 - 리소스가 없는 경우 테스트")
	public void testGetJobGuideByIdNotFound() {
		// Given
		when(jobGuideRepository.findById(anyInt())).thenReturn(Optional.empty());

		// When & Then
		assertThrows(RequestFailedException.class, () -> {
			jobGuideService.getJobGuideById(1);
		});
	}

	@Test
	@DisplayName("새로운 취업가이드 생성 테스트")
	public void testCreateJobGuide() {
		// Given
		JobGuideRequestDTO requestDTO = new JobGuideRequestDTO();
		requestDTO.setUsername("testuser");
		requestDTO.setTitle("Job Guide 1");
		requestDTO.setContent("Content for Job Guide 1");

		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(jobGuideRepository.save(any(JobGuide.class))).thenReturn(jobGuide);

		// When
		JobGuideResponseDTO result = jobGuideService.createJobGuide(requestDTO);

		// Then
		assertThat(result.getTitle()).isEqualTo("Job Guide 1");
		assertThat(result.getContent()).isEqualTo("Content for Job Guide 1");
	}

	@Test
	@DisplayName("새로운 취업가이드 생성 - 사용자 찾기 실패 테스트")
	public void testCreateJobGuideUserNotFound() {
		// Given
		JobGuideRequestDTO requestDTO = new JobGuideRequestDTO();
		requestDTO.setUsername("testuser");
		requestDTO.setTitle("Job Guide 1");
		requestDTO.setContent("Content for Job Guide 1");

		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

		// When & Then
		assertThrows(RequestFailedException.class, () -> {
			jobGuideService.createJobGuide(requestDTO);
		});
	}

	@Test
	@DisplayName("취업가이드 수정 테스트")
	public void testUpdateJobGuide() {
		// Given
		JobGuideRequestDTO requestDTO = new JobGuideRequestDTO();
		requestDTO.setUsername("testuser");
		requestDTO.setTitle("Updated Job Guide");
		requestDTO.setContent("Updated content for Job Guide");

		when(jobGuideRepository.findById(anyInt())).thenReturn(Optional.of(jobGuide));

		// When
		JobGuideResponseDTO result = jobGuideService.updateJobGuide(1, requestDTO);

		// Then
		assertThat(result.getTitle()).isEqualTo("Updated Job Guide");
		assertThat(result.getContent()).isEqualTo("Updated content for Job Guide");
	}

	@Test
	@DisplayName("취업가이드 수정 - 리소스가 없는 경우 테스트")
	public void testUpdateJobGuideNotFound() {
		// Given
		JobGuideRequestDTO requestDTO = new JobGuideRequestDTO();
		requestDTO.setUsername("testuser");
		requestDTO.setTitle("Updated Job Guide");
		requestDTO.setContent("Updated content for Job Guide");

		when(jobGuideRepository.findById(anyInt())).thenReturn(Optional.empty());

		// When & Then
		assertThrows(ResourceNotFoundException.class, () -> {
			jobGuideService.updateJobGuide(1, requestDTO);
		});
	}

	@Test
	@DisplayName("취업가이드 삭제 테스트")
	public void testDeleteJobGuide() {
		// Given
		when(jobGuideRepository.existsById(anyInt())).thenReturn(true);

		// When
		jobGuideService.deleteJobGuide(1);

		// Then
		verify(jobGuideRepository, times(1)).deleteById(1);
	}

	@Test
	@DisplayName("취업가이드 삭제 - 리소스가 없는 경우 테스트")
	public void testDeleteJobGuideNotFound() {
		// Given
		when(jobGuideRepository.existsById(anyInt())).thenReturn(false);

		// When & Then
		assertThrows(RequestFailedException.class, () -> {
			jobGuideService.deleteJobGuide(1);
		});
	}
}