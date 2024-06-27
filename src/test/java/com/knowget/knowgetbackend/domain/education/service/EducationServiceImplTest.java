package com.knowget.knowgetbackend.domain.education.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.knowget.knowgetbackend.domain.education.dto.EducationResponseDTO;
import com.knowget.knowgetbackend.global.exception.RequestFailedException;

class EducationServiceImplTest {
	@Mock
	private RestTemplate restTemplate;

	@Mock
	private RestTemplateBuilder restTemplateBuilder;

	@InjectMocks
	private EducationServiceImpl educationService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		when(restTemplateBuilder.build()).thenReturn(restTemplate);
		educationService = new EducationServiceImpl(restTemplateBuilder);
	}

	@Test
	@DisplayName("모든 교육강의 가져오기 테스트 - getAllEducations")
	void testGetAllEducations() {
		String url = "http://openAPI.seoul.go.kr:8088/4952704e5a737379343850566e5a51/json/OfflineCourse/1/1000";
		String jsonResponse = "{ \"OfflineCourse\": { \"row\": [ { \"COURSE_NM\": \"Sample Course\", \"COURSE_REQUEST_STR_DT\": \"20220101\", \"COURSE_REQUEST_END_DT\": \"20220131\", \"COURSE_STR_DT\": \"20220201\", \"COURSE_END_DT\": \"20220228\", \"CAPACITY\": 30, \"COURSE_APPLY_URL\": \"http://example.com\", \"DEPT_NM\": \"Sample Department\", \"GU\": \"Sample District\" } ] } }";

		ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		doReturn(responseEntity).when(restTemplate).getForEntity(url, String.class);

		List<EducationResponseDTO> result = educationService.getAllEducations();

		assertThat(result).hasSize(1);
		verify(restTemplate, times(1)).getForEntity(url, String.class);
	}

	@Test
	@DisplayName("교육강의 검색하기 테스트 - searchEducations")
	void testSearchEducations() {
		String keyword = "Sample";
		String jsonResponse = "{ \"OfflineCourse\": { \"row\": [ { \"COURSE_NM\": \"Sample Course\", \"COURSE_REQUEST_STR_DT\": \"20220101\", \"COURSE_REQUEST_END_DT\": \"20220131\", \"COURSE_STR_DT\": \"20220201\", \"COURSE_END_DT\": \"20220228\", \"CAPACITY\": 30, \"COURSE_APPLY_URL\": \"http://example.com\", \"DEPT_NM\": \"Sample Department\", \"GU\": \"Sample District\" } ] } }";

		ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		doReturn(responseEntity).when(restTemplate).getForEntity(anyString(), eq(String.class));

		List<EducationResponseDTO> result = educationService.searchEducations(keyword);

		assertThat(result).hasSize(1);
		verify(restTemplate, times(1)).getForEntity(anyString(), eq(String.class));
	}

	@Test
	@DisplayName("모집중인 교육강의 가져오기 테스트 - getRecruitingEducations")
	void testGetRecruitingEducations() {
		String jsonResponse = "{ \"OfflineCourse\": { \"row\": [ { \"COURSE_NM\": \"Sample Course\", \"COURSE_REQUEST_STR_DT\": \"20230101\", \"COURSE_REQUEST_END_DT\": \"20241231\", \"COURSE_STR_DT\": \"20230201\", \"COURSE_END_DT\": \"20230228\", \"CAPACITY\": 30, \"COURSE_APPLY_URL\": \"http://example.com\", \"DEPT_NM\": \"Sample Department\", \"GU\": \"Sample District\" } ] } }";

		when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(
			new ResponseEntity<>(jsonResponse, HttpStatus.OK));

		List<EducationResponseDTO> result = educationService.getRecruitingEducations();

		assertThat(result).hasSize(1);
		verify(restTemplate, times(1)).getForEntity(anyString(), eq(String.class));
	}

	@Test
	@DisplayName("조건에 따른 날짜 필터링 테스트 - dateFiltering")
	void testDateFiltering() {
		assertTrue(educationService.dateFiltering("20220101", "20220131"));
		assertFalse(educationService.dateFiltering("20220101", "20240101")); // 2년 이상 차이
		assertFalse(educationService.dateFiltering("20220101", "20221301")); // 잘못된 월
		assertFalse(educationService.dateFiltering("20220101", "20220132")); // 잘못된 일
	}

	@Test
	@DisplayName("잘못된 API 데이터 가져오기 테스트 - getEducationsFromApi")
	void testGetEducationsFromApiWithError() {
		String url = "http://openAPI.seoul.go.kr:8088/4952704e5a737379343850566e5a51/json/OfflineCourse/1/1000";
		String jsonResponse = "{ \"OfflineCourse\": { \"row\": [] } }";

		ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		doReturn(responseEntity).when(restTemplate).getForEntity(url, String.class);

		List<EducationResponseDTO> result = educationService.getAllEducations();

		assertThat(result).isEmpty();
		verify(restTemplate, times(1)).getForEntity(url, String.class);
	}

	@Test
	@DisplayName("교육강의 검색 결과 없음 테스트 - searchEducations")
	void testSearchEducationsNoResult() {
		String keyword = "NonExistingKeyword";
		String jsonResponse = "{ \"OfflineCourse\": { \"row\": [ { \"COURSE_NM\": \"Sample Course\", \"COURSE_REQUEST_STR_DT\": \"20220101\", \"COURSE_REQUEST_END_DT\": \"20220131\", \"COURSE_STR_DT\": \"20220201\", \"COURSE_END_DT\": \"20220228\", \"CAPACITY\": 30, \"COURSE_APPLY_URL\": \"http://example.com\", \"DEPT_NM\": \"Sample Department\", \"GU\": \"Sample District\" } ] } }";

		ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		doReturn(responseEntity).when(restTemplate).getForEntity(anyString(), eq(String.class));

		assertThrows(RequestFailedException.class, () -> educationService.searchEducations(keyword));
		verify(restTemplate, times(1)).getForEntity(anyString(), eq(String.class));
	}
}