package com.knowget.knowgetbackend.domain.post.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.knowget.knowgetbackend.domain.post.dto.PostRequestDTO;
import com.knowget.knowgetbackend.domain.post.dto.PostResponseDTO;
import com.knowget.knowgetbackend.domain.post.dto.SeoulApiResponse;
import com.knowget.knowgetbackend.domain.post.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

	private final PostService postService;

	// 일자리 찾기 API 키 불러옴 !
	@Value("${seoul.api.key}")
	private String apiKey;

	/**
	 * 공공 API에서 일자리 정보를 가져와 데이터베이스에 저장.
	 * @param startIndex 가져올 데이터의 시작 인덱스 (기본값: 1).
	 * @param endIndex 가져올 데이터의 종료 인덱스 (기본값: 100).
	 * @return 저장 완료 메시지.
	 * @author 윾진
	 */
	@GetMapping("/fetch-job-info")
	public String fetchJobInfo(@RequestParam(defaultValue = "1") int startIndex,
		@RequestParam(defaultValue = "100") int endIndex) {

		// API 호출 URL 생성
		String apiUrl = String.format("http://openapi.seoul.go.kr:8088/%s/json/GetJobInfo/%d/%d",
			apiKey, startIndex, endIndex);

		// RestTemplate 으로 API 호출함..
		RestTemplate restTemplate = new RestTemplate();
		SeoulApiResponse response = restTemplate.getForObject(apiUrl, SeoulApiResponse.class);

		// API 응답이 유효 시 데이터 저장 !
		if (response != null && response.getGetJobInfo() != null) {
			List<PostRequestDTO> postRequestDTOs = response.getGetJobInfo().getRow().stream()
				.map(post -> new PostRequestDTO(
					post.getJoReqstNo(),
					post.getJoRegistNo(),
					post.getCmpnyNm(),
					post.getBsnsSumryCn(),
					post.getRcritJssfcCmmnCodeSe(),
					post.getJobcodeNm(),
					post.getRcritNmprCo(),
					post.getAcdmcrCmmnCodeSe(),
					post.getAcdmcrNm(),
					post.getEmplymStleCmmnCodeSe(),
					post.getEmplymStleCmmnMm(),
					post.getWorkPararBassAdresCn(),
					post.getSubwayNm(),
					post.getDtyCn(),
					post.getCareerCndCmmnCodeSe(),
					post.getCareerCndNm(),
					post.getHopeWage(),
					post.getRetGrantsNm(),
					post.getWorkTimeNm(),
					post.getWorkTmNm(),
					post.getHolidayNm(),
					post.getWeekWorkHr(),
					post.getJoFeinsrSbscrbNm(),
					post.getRceptClosNm(),
					post.getRceptMthIemNm(),
					post.getModelMthNm(),
					post.getRceptMthNm(),
					post.getPresentnPapersNm(),
					post.getMngrNm(),
					post.getMngrPhonNo(),
					post.getMngrInsttNm(),
					post.getBassAdresCn(),
					post.getJoSj(),
					post.getJoRegDt(),
					post.getGuiLn()
				))
				.collect(Collectors.toList());

			// DB에 저장
			postService.savePosts(postRequestDTOs);
		}

		return "데이터 저장 됐심미더";
	}

	/**
	 * 모든 저장된 게시글을 조회.
	 * @return 저장된 모든 게시글 목록.
	 * @author 윾진
	 */
	@GetMapping
	public List<PostResponseDTO> getAllPosts() {
		return postService.getAllPosts();
	}
}
