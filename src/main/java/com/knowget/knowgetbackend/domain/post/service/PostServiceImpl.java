package com.knowget.knowgetbackend.domain.post.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.knowget.knowgetbackend.domain.post.dto.PostRequestDTO;
import com.knowget.knowgetbackend.domain.post.dto.PostResponseDTO;
import com.knowget.knowgetbackend.domain.post.repository.PostRepository;
import com.knowget.knowgetbackend.global.entity.Post;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;

	/**
	 * 게시글 목록을 저장.
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

	/**
	 * 요청 DTO를 엔티티로 변환.
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
