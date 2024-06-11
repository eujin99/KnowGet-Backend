package com.knowget.knowgetbackend.domain.post.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.knowget.knowgetbackend.domain.notification.service.NotificationService;
import com.knowget.knowgetbackend.domain.post.dto.PostRequestDTO;
import com.knowget.knowgetbackend.domain.post.dto.PostResponseDTO;
import com.knowget.knowgetbackend.domain.post.dto.SeoulApiResponse;
import com.knowget.knowgetbackend.domain.post.repository.PostRepository;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.Post;
import com.knowget.knowgetbackend.global.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final NotificationService notificationService;

	@Value("${seoul.api.key}")
	private static String apiKey;

	// 주간 시간대 (오전 11시 59분)
	@Scheduled(cron = "0 59 11 * * ?")
	public void fetchPostsDaytime() {
		fetchPosts();
	}

	// 야간 시간대 (오후 11시 59분)
	@Scheduled(cron = "0 59 23 * * ?")
	public void fetchPostsNighttime() {
		fetchPosts();
	}

	/**
	 * Open API에서 일자리 정보를 가져와 데이터베이스에 저장 및 알림 전송
	 *
	 * @author Jihwan
	 */
	public void fetchPosts() {
		String apiUrl = String.format("http://openapi.seoul.go.kr:8088/%s/json/GetJobInfo/%d/%d", apiKey, 1, 100);
		RestTemplate restTemplate = new RestTemplate();
		SeoulApiResponse responses = restTemplate.getForObject(apiUrl, SeoulApiResponse.class);

		if (responses != null && responses.getGetJobInfo() != null) {
			List<Post> posts = convertToEntities(responses);
			for (Post post : posts) {
				if (!postRepository.existsByJoRegistNo(post.getJoRegistNo())) {
					postRepository.save(post);
					notifyUsers(post);
				}
			}
		}
	}

	/**
	 * 일자리 정보를 저장하고 관심있는 사용자에게 알림 전송
	 *
	 * @param post 저장할 일자리 정보
	 * @author Jihwan
	 */
	private void notifyUsers(Post post) {
		List<User> interestedUsers = userRepository.findByPrefLocationOrPrefJob(post.getWorkPararBassAdresCn(),
			Short.parseShort(post.getRcritJssfcCmmnCodeSe()));

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
		Pattern locationPattern = Pattern.compile("서울\\s(\\S+구)");
		return seoulApiResponse.getGetJobInfo().getRow().stream()
			.map(post -> {
				Matcher locationMatcher = locationPattern.matcher(post.getWorkPararBassAdresCn());
				String gu = locationMatcher.find() ? locationMatcher.group(1) : post.getWorkPararBassAdresCn();
				String jobCode = post.getRcritJssfcCmmnCodeSe()
					.replaceAll("^1\\d{4}|^21\\d{3}|^22\\d{3}|^25\\d{3}|^26\\d{3}|^026\\d{3}|^27\\d{3}"
							+ "|^027\\d{3}|^28\\d{3}|^29\\d{3}|^029\\d{3}|^32\\d{3}|^33\\d{3}|^22\\d{4}",
						"0")
					.replaceAll("^24\\d{3}|^024\\d{3}", "1")
					.replaceAll("^12\\d{4}|^13\\d{4}|^21\\d{4}", "2")
					.replaceAll("^14\\d{4}|^014\\d{3}|^56\\d{4}|^70\\d{4}|^90\\d{4}", "3")
					.replaceAll("^15\\d{4}|^24\\d{4}|^54\\d{4}", "4")
					.replaceAll("^23\\d{4}|^30\\d{4}|^55\\d{4}", "5")
					.replaceAll("^41\\d{4}|^51\\d{4}", "6")
					.replaceAll("^53\\d{4}|^87\\d{4}", "7")
					.replaceAll("^61\\d{4}|^62\\d{4}|^015\\d{3}", "8")
					.replaceAll("^81\\d{4}|^82\\d{4}|^83\\d{4}|^85\\d{4}|^86\\d{4}|^88\\d{4}|^89\\d{4}",
						"9");
				return Post.builder()
					.joReqstNo(post.getJoReqstNo())
					.joRegistNo(post.getJoRegistNo())
					.cmpnyNm(post.getCmpnyNm())
					.bsnsSumryCn(post.getBsnsSumryCn())
					.rcritJssfcCmmnCodeSe(jobCode)
					.jobcodeNm(post.getJobcodeNm())
					.rcritNmprCo(post.getRcritNmprCo())
					.acdmcrCmmnCodeSe(post.getAcdmcrCmmnCodeSe())
					.acdmcrNm(post.getAcdmcrNm())
					.emplymStleCmmnCodeSe(post.getEmplymStleCmmnCodeSe())
					.emplymStleCmmnMm(post.getEmplymStleCmmnMm())
					.workPararBassAdresCn(gu)
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
					.build();
			})
			.collect(Collectors.toList());
	}

	/**
	 * 게시글 목록을 저장.
	 *
	 * @param postRequestDTOs 저장할 게시글 요청 DTO 목록.
	 * @author 윾진
	 */
	@Override
	public void savePosts(List<PostRequestDTO> postRequestDTOs) {
		// 요청 DTO를 엔티티로 변환하여 저장
		List<Post> posts = postRequestDTOs.stream()
			.map(this::convertToEntity)
			.collect(Collectors.toList());
		postRepository.saveAll(posts);
	}

	/**
	 * 모든 게시글을 조회.
	 *
	 * @return 저장된 모든 게시글 응답 DTO 목록.
	 * @author 윾진
	 */
	@Override
	public List<PostResponseDTO> getAllPosts() {
		// 엔티티를 응답 DTO로 변환하여 반환
		return postRepository.findAll().stream()
			.map(this::convertToDTO)
			.collect(Collectors.toList());
	}

	@Override
	public List<PostResponseDTO> getPostsByLocation(String gu) {
		return postRepository.findByWorkPararBassAdresCnContaining(gu).stream()
			.map(this::convertToDTO)
			.collect(Collectors.toList());
	}

	@Override
	public List<PostResponseDTO> getPostsByJobCode(String code) {
		return postRepository.findByRcritJssfcCmmnCodeSe(code).stream()
			.map(this::convertToDTO)
			.collect(Collectors.toList());
	}

	/**
	 * 요청 DTO를 엔티티로 변환.
	 *
	 * @param dto 변환할 요청 DTO.
	 * @return 변환된 엔티티.
	 * @author 윾진
	 */
	private Post convertToEntity(PostRequestDTO dto) {
		return Post.builder()
			.joReqstNo(dto.getJoReqstNo())
			.joRegistNo(dto.getJoRegistNo())
			.cmpnyNm(dto.getCmpnyNm())
			.bsnsSumryCn(dto.getBsnsSumryCn())
			.rcritJssfcCmmnCodeSe(dto.getRcritJssfcCmmnCodeSe())
			.jobcodeNm(dto.getJobcodeNm())
			.rcritNmprCo(dto.getRcritNmprCo())
			.acdmcrCmmnCodeSe(dto.getAcdmcrCmmnCodeSe())
			.acdmcrNm(dto.getAcdmcrNm())
			.emplymStleCmmnCodeSe(dto.getEmplymStleCmmnCodeSe())
			.emplymStleCmmnMm(dto.getEmplymStleCmmnMm())
			.workPararBassAdresCn(dto.getWorkPararBassAdresCn())
			.subwayNm(dto.getSubwayNm())
			.dtyCn(dto.getDtyCn())
			.careerCndCmmnCodeSe(dto.getCareerCndCmmnCodeSe())
			.careerCndNm(dto.getCareerCndNm())
			.hopeWage(dto.getHopeWage())
			.retGrantsNm(dto.getRetGrantsNm())
			.workTimeNm(dto.getWorkTimeNm())
			.workTmNm(dto.getWorkTmNm())
			.holidayNm(dto.getHolidayNm())
			.weekWorkHr(dto.getWeekWorkHr())
			.joFeinsrSbscrbNm(dto.getJoFeinsrSbscrbNm())
			.rceptClosNm(dto.getRceptClosNm())
			.rceptMthIemNm(dto.getRceptMthIemNm())
			.modelMthNm(dto.getModelMthNm())
			.rceptMthNm(dto.getRceptMthNm())
			.presentnPapersNm(dto.getPresentnPapersNm())
			.mngrNm(dto.getMngrNm())
			.mngrPhonNo(dto.getMngrPhonNo())
			.mngrInsttNm(dto.getMngrInsttNm())
			.bassAdresCn(dto.getBassAdresCn())
			.joSj(dto.getJoSj())
			.joRegDt(dto.getJoRegDt())
			.guiLn(dto.getGuiLn())
			.build();
	}

	/**
	 * 엔티티를 응답 DTO로 변환.
	 *
	 * @param entity 변환할 엔티티.
	 * @return 변환된 응답 DTO.
	 * @author 윾진
	 */
	private PostResponseDTO convertToDTO(Post entity) {
		return new PostResponseDTO(
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
			entity.getGuiLn()
		);
	}

}
