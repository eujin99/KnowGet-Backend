package com.knowget.knowgetbackend.domain.post.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.knowget.knowgetbackend.domain.notification.service.NotificationService;
import com.knowget.knowgetbackend.domain.post.dto.PostResponseDTO;
import com.knowget.knowgetbackend.domain.post.dto.SeoulApiResponse;
import com.knowget.knowgetbackend.domain.post.repository.PostRepository;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.Post;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.exception.PostNotFoundException;
import com.knowget.knowgetbackend.global.exception.RequestFailedException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final NotificationService notificationService;

	@Value("${seoul.api.key}")
	private String apiKey;

	private static final Map<Pattern, String> JOB_CODE_PATTERNS = createJobCodePatterns();

	// 매 시 00분 마다 일자리 정보를 가져와 데이터베이스에 저장 및 알림 전송
	@Scheduled(cron = "0 0 * * * ?")
	@Transactional
	public void scheduledFetch() {
		log.info("post fetching started at {}", LocalDateTime.now());
		int insertCount = fetchPosts(1, 100);
		log.info("fetched {} posts at {}", insertCount, LocalDateTime.now());
	}

	/**
	 * Open API에서 일자리 정보를 가져와 데이터베이스에 저장 및 알림 전송
	 *
	 * @return 저장된 게시글 개수
	 * @author Jihwan
	 */
	@Override
	@Transactional
	public Integer fetchPosts(int startIndex, int endIndex) {
		String apiUrl = String.format("http://openapi.seoul.go.kr:8088/%s/json/GetJobInfo/%d/%d", apiKey, startIndex,
			endIndex);
		int insertCount = 0;

		RestTemplate restTemplate = new RestTemplate();
		SeoulApiResponse responses = restTemplate.getForObject(apiUrl, SeoulApiResponse.class);

		if (responses != null && responses.getGetJobInfo() != null) {
			List<Post> posts = convertToEntities(responses);
			for (Post post : posts) {
				if (!postRepository.existsByJoRegistNo(post.getJoRegistNo())) {
					postRepository.save(post);
					notifyUsers(post);
					insertCount++;
				}
			}
		}
		return insertCount;
	}

	/**
	 * 일자리 정보를 저장하고 관심있는 사용자에게 알림 전송
	 *
	 * @param post 저장할 일자리 정보
	 * @author Jihwan
	 */
	@Override
	@Transactional
	public void notifyUsers(Post post) {
		List<User> interestedUsers = userRepository.findByPrefLocationOrPrefJob(post.getGu(), (post.getJobCode()));

		for (User user : interestedUsers) {
			notificationService.sendNotification(user, post);
		}
	}

	/**
	 * API 응답을 엔터티로 변환
	 *
	 * @param seoulApiResponse API 응답
	 * @return 변환된 엔터티 List
	 * @author Jihwan
	 * @see Post
	 * @see SeoulApiResponse
	 */
	private List<Post> convertToEntities(SeoulApiResponse seoulApiResponse) {
		return seoulApiResponse.getGetJobInfo().getRow().stream()
			.map(post -> {
				String gu = extractGu(post.getGuiLn());
				String jobCode = convertJobCode(post.getRcritJssfcCmmnCodeSe());

				return Post.builder()
					.joReqstNo(post.getJoReqstNo())
					.joRegistNo(post.getJoRegistNo())
					.cmpnyNm(post.getCmpnyNm())
					.bsnsSumryCn(post.getBsnsSumryCn())
					.rcritJssfcCmmnCodeSe(post.getRcritJssfcCmmnCodeSe())
					.jobcodeNm(post.getJobcodeNm())
					.rcritNmprCo(post.getRcritNmprCo())
					.acdmcrCmmnCodeSe(post.getAcdmcrCmmnCodeSe())
					.acdmcrNm(post.getAcdmcrNm())
					.emplymStleCmmnCodeSe(post.getEmplymStleCmmnCodeSe())
					.emplymStleCmmnMm(post.getEmplymStleCmmnMm())
					.workPararBassAdresCn(post.getWorkPararBassAdresCn())
					.subwayNm(post.getSubwayNm())
					.dtyCn(post.getDtyCn())
					.careerCndCmmnCodeSe(post.getCareerCndCmmnCodeSe())
					.careerCndNm(post.getCareerCndNm())
					.hopeWage(post.getHopeWage())
					.retGrantsNm(post.getRetGrantsNm())
					.workTimeNm(post.getWorkTimeNm())
					.workTmNm(post.getWorkTmNm())
					.holidayNm(post.getHolidayNm())
					.weekWorkHr(post.getWeekWorkHr())
					.joFeinsrSbscrbNm(post.getJoFeinsrSbscrbNm())
					.rceptClosNm(post.getRceptClosNm())
					.rceptMthIemNm(post.getRceptMthIemNm())
					.modelMthNm(post.getModelMthNm())
					.rceptMthNm(post.getRceptMthNm())
					.presentnPapersNm(post.getPresentnPapersNm())
					.mngrNm(post.getMngrNm())
					.mngrPhonNo(post.getMngrPhonNo())
					.mngrInsttNm(post.getMngrInsttNm())
					.bassAdresCn(post.getBassAdresCn())
					.joSj(post.getJoSj())
					.joRegDt(post.getJoRegDt())
					.guiLn(post.getGuiLn())
					.gu(gu)
					.jobCode(jobCode)
					.build();
			})
			.collect(Collectors.toList());
	}

	/**
	 * 구 이름을 추출
	 *
	 * @param guiLn 구 이름이 포함된 문자열 (e.g., "GUI_LN": "(월급)206만원 / 서울 강남구 / 경력 무관")
	 * @return 추출된 구 이름
	 * @author Jihwan
	 */
	protected String extractGu(String guiLn) {
		Pattern locationPattern = Pattern.compile("서울\\s(\\S+구)");
		Matcher locationMatcher = locationPattern.matcher(guiLn);
		if (locationMatcher.find()) {
			return locationMatcher.group(1);
		} else {
			// 구분자로 '/'을 사용하여 가운데 값을 추출
			String[] parts = guiLn.split("/");
			if (parts.length == 3) {
				return parts[1].trim();
			}
		}
		return guiLn; // 매칭되지 않는 경우 원래 문자열 반환
	}

	/**
	 * 직종 코드 패턴을 생성
	 *
	 * @return 직종 코드 패턴 Map
	 * @author Jihwan
	 * @see #convertJobCode(String)
	 * @see #JOB_CODE_PATTERNS
	 */
	private static Map<Pattern, String> createJobCodePatterns() {
		Map<Pattern, String> patterns = new HashMap<>();
		patterns.put(Pattern.compile("^24\\d{3}|^024\\d{3}"), "1");
		patterns.put(Pattern.compile("^12\\d{4}|^13\\d{4}|^21\\d{4}"), "2");
		patterns.put(Pattern.compile("^14\\d{4}|^014\\d{3}|^56\\d{4}|^70\\d{4}|^90\\d{4}"), "3");
		patterns.put(Pattern.compile("^15\\d{4}|^24\\d{4}|^54\\d{4}"), "4");
		patterns.put(Pattern.compile("^23\\d{4}|^30\\d{4}|^55\\d{4}"), "5");
		patterns.put(Pattern.compile("^41\\d{4}|^51\\d{4}"), "6");
		patterns.put(Pattern.compile("^53\\d{4}|^87\\d{4}"), "7");
		patterns.put(Pattern.compile("^61\\d{4}|^62\\d{4}|^015\\d{3}"), "8");
		patterns.put(Pattern.compile("^81\\d{4}|^82\\d{4}|^83\\d{4}|^85\\d{4}|^86\\d{4}|^88\\d{4}|^89\\d{4}"), "9");
		patterns.put(Pattern.compile("^1\\d{4}|^21\\d{3}|^22\\d{3}|^25\\d{3}|^26\\d{3}|^026\\d{3}|^27\\d{3}"
			+ "|^027\\d{3}|^28\\d{3}|^29\\d{3}|^029\\d{3}|^32\\d{3}|^33\\d{3}|^22\\d{4}"), "0");
		return patterns;
	}

	/**
	 * 직종 코드를 변환
	 *
	 * @param code 변환할 직종 코드
	 * @return 변환된 직종 코드
	 * @author Jihwan
	 * @see #JOB_CODE_PATTERNS
	 * @see #createJobCodePatterns()
	 */
	protected String convertJobCode(String code) {
		for (Map.Entry<Pattern, String> entry : JOB_CODE_PATTERNS.entrySet()) {
			if (entry.getKey().matcher(code).find()) {
				return entry.getValue();
			}
		}
		return code; // 매칭되지 않는 경우 원래 코드를 반환
	}

	// /**
	//  * 게시글 목록을 저장.
	//  *
	//  * @param postRequestDTOs 저장할 게시글 요청 DTO 목록.
	//  * @author 윾진
	//  */
	// @Override
	// public void savePosts(List<PostRequestDTO> postRequestDTOs) {
	// 	// 요청 DTO를 엔티티로 변환하여 저장
	// 	List<Post> posts = postRequestDTOs.stream()
	// 		.map(this::convertToEntity)
	// 		.collect(Collectors.toList());
	// 	postRepository.saveAll(posts);
	// }

	/**
	 * 모든 게시글을 조회.
	 *
	 * @return 저장된 모든 게시글 응답 DTO 목록.
	 * @author 윾진
	 */
	@Override
	@Transactional(readOnly = true)
	public List<PostResponseDTO> getAllPosts() {
		// 엔티티를 응답 DTO로 변환하여 반환
		return postRepository.findAllByOrderByPostIdDesc().stream()
			.map(this::convertToDTO)
			.collect(Collectors.toList());
	}

	/**
	 * 일자리 공고 ID로 상세 조회
	 *
	 * @param postId 일자리 공고 ID
	 * @return 일자리 공고 상세 정보
	 * @author Jihwan
	 */
	@Override
	public PostResponseDTO getPostById(Integer postId) {
		try {
			Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("존재하지 않는 게시글입니다"));
			return convertToDTO(post);
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 게시글 조회에 실패하였습니다 : " + e.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<PostResponseDTO> getPostsByLocation(String gu) {
		return postRepository.findByWorkPararBassAdresCnContainingOrderByPostIdDesc(gu).stream()
			.map(this::convertToDTO)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<PostResponseDTO> getPostsByJobCode(String code) {
		return postRepository.findByRcritJssfcCmmnCodeSeOrderByPostIdDesc(code).stream()
			.map(this::convertToDTO)
			.collect(Collectors.toList());
	}

	// /**
	//  * 요청 DTO를 엔티티로 변환.
	//  *
	//  * @param dto 변환할 요청 DTO.
	//  * @return 변환된 엔티티.
	//  * @author 윾진
	//  */
	// private Post convertToEntity(PostRequestDTO dto) {
	// 	return Post.builder()
	// 		.joReqstNo(dto.getJoReqstNo())
	// 		.joRegistNo(dto.getJoRegistNo())
	// 		.cmpnyNm(dto.getCmpnyNm())
	// 		.bsnsSumryCn(dto.getBsnsSumryCn())
	// 		.rcritJssfcCmmnCodeSe(dto.getRcritJssfcCmmnCodeSe())
	// 		.jobcodeNm(dto.getJobcodeNm())
	// 		.rcritNmprCo(dto.getRcritNmprCo())
	// 		.acdmcrCmmnCodeSe(dto.getAcdmcrCmmnCodeSe())
	// 		.acdmcrNm(dto.getAcdmcrNm())
	// 		.emplymStleCmmnCodeSe(dto.getEmplymStleCmmnCodeSe())
	// 		.emplymStleCmmnMm(dto.getEmplymStleCmmnMm())
	// 		.workPararBassAdresCn(dto.getWorkPararBassAdresCn())
	// 		.subwayNm(dto.getSubwayNm())
	// 		.dtyCn(dto.getDtyCn())
	// 		.careerCndCmmnCodeSe(dto.getCareerCndCmmnCodeSe())
	// 		.careerCndNm(dto.getCareerCndNm())
	// 		.hopeWage(dto.getHopeWage())
	// 		.retGrantsNm(dto.getRetGrantsNm())
	// 		.workTimeNm(dto.getWorkTimeNm())
	// 		.workTmNm(dto.getWorkTmNm())
	// 		.holidayNm(dto.getHolidayNm())
	// 		.weekWorkHr(dto.getWeekWorkHr())
	// 		.joFeinsrSbscrbNm(dto.getJoFeinsrSbscrbNm())
	// 		.rceptClosNm(dto.getRceptClosNm())
	// 		.rceptMthIemNm(dto.getRceptMthIemNm())
	// 		.modelMthNm(dto.getModelMthNm())
	// 		.rceptMthNm(dto.getRceptMthNm())
	// 		.presentnPapersNm(dto.getPresentnPapersNm())
	// 		.mngrNm(dto.getMngrNm())
	// 		.mngrPhonNo(dto.getMngrPhonNo())
	// 		.mngrInsttNm(dto.getMngrInsttNm())
	// 		.bassAdresCn(dto.getBassAdresCn())
	// 		.joSj(dto.getJoSj())
	// 		.joRegDt(dto.getJoRegDt())
	// 		.guiLn(dto.getGuiLn())
	// 		.gu(dto.getGu())
	// 		.jobCode(dto.getJobCode())
	// 		.build();
	// }

	/**
	 * 엔티티를 응답 DTO로 변환.
	 *
	 * @param entity 변환할 엔티티.
	 * @return 변환된 응답 DTO.
	 * @author 윾진
	 */
	private PostResponseDTO convertToDTO(Post entity) {
		return new PostResponseDTO(
			entity.getPostId(),
			entity.getJoReqstNo(),
			entity.getJoRegistNo(),
			entity.getCmpnyNm(),
			entity.getBsnsSumryCn(),
			entity.getRcritJssfcCmmnCodeSe(),
			entity.getJobcodeNm(),
			entity.getRcritNmprCo(),
			entity.getAcdmcrCmmnCodeSe(),
			entity.getAcdmcrNm(),
			entity.getEmplymStleCmmnCodeSe(),
			entity.getEmplymStleCmmnMm(),
			entity.getWorkPararBassAdresCn(),
			entity.getSubwayNm(),
			entity.getDtyCn(),
			entity.getCareerCndCmmnCodeSe(),
			entity.getCareerCndNm(),
			entity.getHopeWage(),
			entity.getRetGrantsNm(),
			entity.getWorkTimeNm(),
			entity.getWorkTmNm(),
			entity.getHolidayNm(),
			entity.getWeekWorkHr(),
			entity.getJoFeinsrSbscrbNm(),
			entity.getRceptClosNm(),
			entity.getRceptMthIemNm(),
			entity.getModelMthNm(),
			entity.getRceptMthNm(),
			entity.getPresentnPapersNm(),
			entity.getMngrNm(),
			entity.getMngrPhonNo(),
			entity.getMngrInsttNm(),
			entity.getBassAdresCn(),
			entity.getJoSj(),
			entity.getJoRegDt(),
			entity.getGuiLn(),
			entity.getGu(),
			entity.getJobCode()
		);
	}

}
