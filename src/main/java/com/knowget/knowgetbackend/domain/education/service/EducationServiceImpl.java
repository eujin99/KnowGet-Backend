package com.knowget.knowgetbackend.domain.education.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.knowget.knowgetbackend.domain.education.dto.EducationResponseDTO;
import com.knowget.knowgetbackend.domain.education.repository.EducationRepository;
import com.knowget.knowgetbackend.global.entity.Education;
import com.knowget.knowgetbackend.global.exception.RequestFailedException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {

	private final EducationRepository educationRepository;

	@Value("${seoul.api.key}")
	private String apiKey;

	/**
	 * 1. 모든 교육강의 가져오기
	 *
	 * @param -
	 * @return List<EducationResponseDTO> 모든 교육강의 리스트
	 * @author MJ
	 */
	@Override
	@Transactional
	public List<EducationResponseDTO> getAllEducations() {
		String apiUrl = String.format("http://openapi.seoul.go.kr:8088/%s/json/OfflineCourse/1/1000", apiKey);
		return getEducationsFromApi(apiUrl);
	}

	@Override
	public List<EducationResponseDTO> getAllEducations2() {
		return educationRepository.findAllByOrderByEducationIdDesc().stream()
				.map(education -> EducationResponseDTO.builder()
						.courseNm(education.getCourseNm())
						.courseRequestStrDt(education.getCourseRequestStrDt())
						.courseRequestEndDt(education.getCourseRequestEndDt())
						.courseStrDt(education.getCourseStrDt())
						.courseEndDt(education.getCourseEndDt())
						.capacity(education.getCapacity())
						.courseApplyUrl(education.getCourseApplyUrl())
						.deptNm(education.getDeptNm())
						.gu(education.getGu())
						.build()
				)
				.collect(Collectors.toList());
	}

	@Transactional
	public void scheduledFetch() {
		log.info("education fetching started at {}", LocalDate.now());
		int insertCount = fetchEducations(1, 1000);
		log.info("fetched {} posts at {}", insertCount, LocalDate.now());
	}

	@Override
	@Transactional
	public List<EducationResponseDTO> searchEducations(String keyword) {
		List<EducationResponseDTO> result = getAllEducations().stream()
				.filter(education -> education.getCourseNm().contains(keyword))
				.toList();

		if (result.isEmpty()) {
			throw new RequestFailedException("검색 결과가 없습니다.");
		}

		return result;
	}

	@Override
	@Transactional
	public List<EducationResponseDTO> getRecruitingEducations() {
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyyMMdd");
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

		return getAllEducations().stream()
				.filter(education -> {
					LocalDate start;
					LocalDate end;
					try {
						start = LocalDate.parse(education.getCourseRequestStrDt(), formatter1);
						end = LocalDate.parse(education.getCourseRequestEndDt(), formatter1);
					} catch (DateTimeParseException e1) {
						try {
							start = LocalDate.parse(education.getCourseRequestStrDt(), formatter2);
							end = LocalDate.parse(education.getCourseRequestEndDt(), formatter2);
						} catch (DateTimeParseException e2) {
							return false;
						}
					}
					return (today.isAfter(start) || today.isEqual(start)) && (today.isBefore(end) || today.isEqual(end));
				})
				.collect(Collectors.toList());
	}

	protected List<EducationResponseDTO> getEducationsFromApi(String url) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			String body = response.getBody();
			if (body != null) {
				try {
					JSONObject jsonObject = new JSONObject(body);
					JSONObject offlineCourse = jsonObject.getJSONObject("OfflineCourse");
					JSONArray jsonArray = offlineCourse.getJSONArray("row");

					List<EducationResponseDTO> educations = new ArrayList<>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject educationJson = jsonArray.getJSONObject(i);
						String courseRequestStrDt = educationJson.getString("COURSE_REQUEST_STR_DT");
						String courseRequestEndDt = educationJson.getString("COURSE_REQUEST_END_DT");

						if (dateFiltering(courseRequestStrDt, courseRequestEndDt)) {
							EducationResponseDTO education = new EducationResponseDTO(
									educationJson.getString("COURSE_NM"),
									courseRequestStrDt,
									courseRequestEndDt,
									educationJson.getString("COURSE_STR_DT"),
									educationJson.getString("COURSE_END_DT"),
									parseCapacity(educationJson),
									educationJson.getString("COURSE_APPLY_URL"),
									educationJson.getString("DEPT_NM"),
									educationJson.getString("GU")
							);
							educations.add(education);
						}
					}
					return educations;
				} catch (Exception e) {
					throw new RuntimeException("OpenAPI response 파싱 실패", e);
				}
			}
		}
		throw new RuntimeException("OpenAPI 데이터 가져오기 실패");
	}

	private Integer parseCapacity(JSONObject educationJson) {
		try {
			return educationJson.getInt("CAPACITY");
		} catch (Exception e) {
			return null; // CAPACITY 필드가 없거나 비어있을 경우 null 반환
		}
	}

	protected boolean dateFiltering(String courseRequestStrDt, String courseRequestEndDt) {
		try {
			int startYear = Integer.parseInt(courseRequestStrDt.substring(0, 4));
			int endYear = Integer.parseInt(courseRequestEndDt.substring(0, 4));
			int startMonth = Integer.parseInt(courseRequestStrDt.substring(4, 6));
			int endMonth = Integer.parseInt(courseRequestEndDt.substring(4, 6));
			int startDate = Integer.parseInt(courseRequestStrDt.substring(6, 8));
			int endDate = Integer.parseInt(courseRequestEndDt.substring(6, 8));

			if (Math.abs(endYear - startYear) >= 2 || (startMonth > 12 || endMonth > 12) || (startDate > 31 || endDate > 31)) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public Integer fetchEducations(int startIndex, int endIndex) {
		String apiUrl = String.format("http://openapi.seoul.go.kr:8088/%s/json/OfflineCourse/%d/%d", apiKey, startIndex, endIndex);
		int insertCount = 0;

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> responses = restTemplate.getForEntity(apiUrl, String.class);

		if (responses.getStatusCode() == HttpStatus.OK) {
			String body = responses.getBody();
			if (body != null) {
				try {
					JSONArray jsonArray = new JSONObject(body).getJSONObject("OfflineCourse").getJSONArray("row");
					log.info("jsonArray : {}", jsonArray);
					log.info("jsonArray.length() : {}", jsonArray.length());

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject educationJson = jsonArray.getJSONObject(i);
						String courseRequestStrDt = educationJson.getString("COURSE_REQUEST_STR_DT");
						String courseRequestEndDt = educationJson.getString("COURSE_REQUEST_END_DT");

						if (dateFiltering(courseRequestStrDt, courseRequestEndDt)) {
							Education education = Education.builder()
									.courseNm(educationJson.getString("COURSE_NM"))
									.courseRequestStrDt(courseRequestStrDt)
									.courseRequestEndDt(courseRequestEndDt)
									.courseStrDt(educationJson.getString("COURSE_STR_DT"))
									.courseEndDt(educationJson.getString("COURSE_END_DT"))
									.capacity(parseCapacity(educationJson))
									.courseApplyUrl(educationJson.getString("COURSE_APPLY_URL"))
									.deptNm(educationJson.getString("DEPT_NM"))
									.gu(educationJson.getString("GU"))
									.build();
							educationRepository.save(education);
							insertCount++;
						}
					}
				} catch (Exception e) {
					throw new RuntimeException("OpenAPI response 파싱 실패", e);
				}
			}
		}
		return insertCount;
	}
}
