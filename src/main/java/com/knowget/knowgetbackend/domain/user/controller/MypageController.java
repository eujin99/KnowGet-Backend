package com.knowget.knowgetbackend.domain.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.knowget.knowgetbackend.domain.counseling.dto.CounselingResponseDTO;
import com.knowget.knowgetbackend.domain.post.dto.PostResponseDTO;
import com.knowget.knowgetbackend.domain.user.dto.JobUpdateDTO;
import com.knowget.knowgetbackend.domain.user.dto.LocationUpdateDTO;
import com.knowget.knowgetbackend.domain.user.dto.PasswordUpdateDTO;
import com.knowget.knowgetbackend.domain.user.dto.WrittenSuccessCaseDTO;
import com.knowget.knowgetbackend.domain.user.service.MypageService;
import com.knowget.knowgetbackend.global.dto.ResultMessageDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/mypage")
@RequiredArgsConstructor
public class MypageController {

	private final MypageService mypageService;

	/**
	 * 사용자의 북마크 목록 조회
	 *
	 * @return 북마크 목록
	 * @author Jihwan
	 * @see PostResponseDTO
	 */
	@GetMapping("/bookmark")
	public ResponseEntity<List<PostResponseDTO>> getBookmarkList() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		List<PostResponseDTO> bookmarkList = mypageService.getBookmarkList(username);
		return ResponseEntity.ok(bookmarkList);
	}

	/**
	 * 사용자의 희망 근무지역을 변경
	 *
	 * @param locationUpdateDTO location : 변경할 근무지역
	 * @return 변경 성공 여부 메시지
	 * @author Jihwan
	 */
	@PatchMapping("/pref-location")
	public ResponseEntity<ResultMessageDTO> updatePrefLocation(@RequestBody LocationUpdateDTO locationUpdateDTO) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		locationUpdateDTO.setUsername(username);
		String message = mypageService.updatePrefLocation(locationUpdateDTO);
		return new ResponseEntity<>(new ResultMessageDTO(message), HttpStatus.OK);
	}

	/**
	 * 사용자의 희망 직종을 변경
	 *
	 * @param jobUpdateDTO job : 변경할 직종
	 * @return 변경 성공 여부 메시지
	 * @author 근엽
	 */
	@PatchMapping("/pref-job")
	public ResponseEntity<ResultMessageDTO> updatePrefJob(@RequestBody JobUpdateDTO jobUpdateDTO) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		jobUpdateDTO.setUsername(username);
		String message = mypageService.updatePrefJob(jobUpdateDTO);
		return new ResponseEntity<>(new ResultMessageDTO(message), HttpStatus.OK);
	}

	/**
	 * 비밀번호 변경
	 *
	 * @param passwordUpdateDTO password : 변경할 비밀번호
	 * @return 변경 성공 여부 메시지
	 * @author 근엽
	 */
	@PatchMapping("/password")
	public ResponseEntity<ResultMessageDTO> updatePassword(@RequestBody PasswordUpdateDTO passwordUpdateDTO) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		passwordUpdateDTO.setUsername(username);
		String message = mypageService.updatePassword(passwordUpdateDTO);
		return new ResponseEntity<>(new ResultMessageDTO(message), HttpStatus.OK);
	}

	/**
	 * 사용자가 등록한 상담 목록 조회
	 *
	 * @return 상담 목록
	 * @author Jihwan
	 * @see CounselingResponseDTO
	 */
	@GetMapping("/counseling")
	public ResponseEntity<List<CounselingResponseDTO>> getCounselingList() {
		List<CounselingResponseDTO> counselingResponseDTOList = mypageService.getCounselingList();
		return ResponseEntity.ok(counselingResponseDTOList);
	}

	/**
	 * 사용자가 작성한 취업 성공사례 게시글 목록 조회
	 *
	 * @return 사용자가 작성한 취업 성공사례 게시글 목록
	 * @author Jihwan
	 * @see WrittenSuccessCaseDTO
	 */
	@GetMapping("/success-case")
	public ResponseEntity<List<WrittenSuccessCaseDTO>> getSuccessList() {

		return new ResponseEntity<>(mypageService.getAllSuccessList(), HttpStatus.OK);
	}

}
