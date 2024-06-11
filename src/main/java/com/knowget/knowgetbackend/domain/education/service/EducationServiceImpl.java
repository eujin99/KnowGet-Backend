package com.knowget.knowgetbackend.domain.education.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.knowget.knowgetbackend.domain.education.dto.EducationResponseDTO;
import com.knowget.knowgetbackend.global.exception.RequestFailedException;

@Service
public class EducationServiceImpl implements EducationService {
	private final RestTemplate restTemplate;
	private final String apiKey = "4952704e5a737379343850566e5a51";
	private final String baseUrl = "http://openAPI.seoul.go.kr:8088/" + apiKey + "/json/OfflineCourse";

	public EducationServiceImpl(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	// 1. 모든 교육강의 가져오기
	@Override
	public List<EducationResponseDTO> getAllEducations() {
		String url = baseUrl + "/1/100"; // 1000번째까지 가져오기
		return getEducationsFromApi(url);
	}

	// 2. 교육강의 검색하기
	@Override
	public List<EducationResponseDTO> searchEducations(String keyword) {
		List<EducationResponseDTO> result = getAllEducations().stream() // 모든 교육강의 stream으로
			.filter(education -> education.getCourseNm().contains(keyword)) // education의 courseNm에 keyword가 포함되어있는지.
			// education data -> stream화 --> filtering --> list화
			.toList();

		// 결과가 없을 때, 결과 메시지(resultMessageDTO) 반환
		if (result.isEmpty()) {
			throw new RequestFailedException("검색 결과가 없습니다.");
		}

		return result;
	}

	// 3. 모집중인 교육강의 가져오기
	@Override
	public List<EducationResponseDTO> getRecruitingEducations() {
		LocalDate today = LocalDate.now();
		// openAPI - courseRequestStrDt, courseRequestEndDt가 'yyyyMMdd' or 'yyyyMMddHHmm' 형식으로 되어있음
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyyMMdd"); // 'yyyyMMdd' 형식 - type 1
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyyMMddHHmm"); // 'yyyyMMddHHmm' 형식 - type 2

		return getAllEducations().stream()
			.filter(education -> {
				LocalDate start; // 시작일
				LocalDate end; // 종료일
				try {
					start = LocalDate.parse(education.getCourseRequestStrDt(), formatter1);
					end = LocalDate.parse(education.getCourseRequestEndDt(), formatter1); // type 1
				} catch (DateTimeParseException e1) { // type 1에서의 날짜 형식 오류
					try {
						start = LocalDate.parse(education.getCourseRequestStrDt(), formatter2);
						end = LocalDate.parse(education.getCourseRequestEndDt(), formatter2); // type 2
					} catch (DateTimeParseException e2) { // type 2에서의 날짜 형식 오류
						return false;
					}
				}
				return (today.isAfter(start) || today.isEqual(start)) && (today.isBefore(end) || today.isEqual(
					end)); // today가 start date 이후이거나 같은 것 "이면서" today가 end date 이전이거나 같은 것
			})
			.collect(Collectors.toList()); // stream to List
	}

	// OpenAPI로부터 교육강의 데이터를 가져오는 메소드
	// 사용 이유: OpenAPI로부터 데이터를 가져오는 과정이 중복되기 때문에 메소드로 분리
	private List<EducationResponseDTO> getEducationsFromApi(String url) {
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			String body = response.getBody();
			if (body != null) {
				try {
					JSONObject jsonObject = new JSONObject(body);
					JSONObject offlineCourse = jsonObject.getJSONObject("OfflineCourse");
					JSONArray jsonArray = offlineCourse.getJSONArray(
						"row");

					List<EducationResponseDTO> educations = new ArrayList<>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject educationJson = jsonArray.getJSONObject(i);
						EducationResponseDTO education = new EducationResponseDTO(
							educationJson.getString("COURSE_NM"),
							educationJson.getString("COURSE_REQUEST_STR_DT"),
							educationJson.getString("COURSE_REQUEST_END_DT"),
							educationJson.getString("COURSE_STR_DT"),
							educationJson.getString("COURSE_END_DT"),
							educationJson.getInt("CAPACITY"),
							educationJson.getString("COURSE_APPLY_URL"),
							educationJson.getString("DEPT_NM"),
							educationJson.getString("GU")
						);
						educations.add(education);
					}
					return educations;
				} catch (JSONException e) {
					throw new RuntimeException("OpenAPI response 파싱 실패", e);
				}
			}
		}
		throw new RuntimeException("OpenAPI 데이터 가져오기 실패");
	}
}