package com.knowget.knowgetbackend.domain.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knowget.knowgetbackend.domain.bookmark.repository.BookmarkRepository;
import com.knowget.knowgetbackend.domain.counseling.dto.CounselingResponseDTO;
import com.knowget.knowgetbackend.domain.counseling.repository.CounselingRepository;
import com.knowget.knowgetbackend.domain.post.dto.PostResponseDTO;
import com.knowget.knowgetbackend.domain.successCase.repository.SuccessCaseRepository;
import com.knowget.knowgetbackend.domain.user.dto.JobUpdateDTO;
import com.knowget.knowgetbackend.domain.user.dto.LocationUpdateDTO;
import com.knowget.knowgetbackend.domain.user.dto.PasswordUpdateDTO;
import com.knowget.knowgetbackend.domain.user.dto.UserInfoDTO;
import com.knowget.knowgetbackend.domain.user.dto.WrittenSuccessCaseDTO;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.Bookmark;
import com.knowget.knowgetbackend.global.entity.Counseling;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.exception.RequestFailedException;
import com.knowget.knowgetbackend.global.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MypageServiceImpl implements MypageService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final SuccessCaseRepository successCaseRepository;
	private final CounselingRepository counselingRepository;
	private final BookmarkRepository bookmarkRepository;

	/**
	 * 사용자가 북마크한 공고 목록 조회
	 *
	 * @return 북마크 목록
	 * @throws RequestFailedException 북마크 목록 조회에 실패했을 때
	 * @author Jihwan
	 * @see PostResponseDTO
	 */
	@Override
	@Transactional(readOnly = true)
	public List<PostResponseDTO> getBookmarkList() {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다"));
			return bookmarkRepository.findByUser(user).stream()
				.map(Bookmark::getPost)
				.toList()
				.stream()
				.map(PostResponseDTO::new)
				.collect(Collectors.toList());
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 북마크 목록 조회에 실패하였습니다 : " + e.getMessage());
		}
	}

	/**
	 * 사용자의 근무 희망 지역을 변경
	 *
	 * @param locationUpdateDTO username : 사용자 ID, location : 변경할 근무지역
	 * @return 변경 성공 여부 메시지
	 * @throws UserNotFoundException  사용자를 찾을 수 없을 때
	 * @throws RequestFailedException 희망 근무지역 변경에 실패했을 때
	 * @author Jihwan
	 */
	@Override
	@Transactional
	public String updatePrefLocation(LocationUpdateDTO locationUpdateDTO) {
		try {
			User user = userRepository.findByUsername(locationUpdateDTO.getUsername())
				.orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다"));
			user.updatePrefLocation(locationUpdateDTO.getLocation());
			userRepository.save(user);
			return "근무 희망 지역이 변경되었습니다";
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 근무 희망 지역 변경에 실패하였습니다 : " + e.getMessage());
		}
	}

	/**
	 * 사용자의 근무 희망 직종을 변경
	 *
	 * @param jobUpdateDTO username : 사용자 ID, job : 변경할 직종
	 * @return 변경 성공 여부 메시지
	 * @throws UserNotFoundException  사용자를 찾을 수 없을 때
	 * @throws RequestFailedException 희망 근무직종 변경에 실패했을 때
	 * @author 근엽
	 * @see JobUpdateDTO
	 */
	@Override
	@Transactional
	public String updatePrefJob(JobUpdateDTO jobUpdateDTO) {
		try {
			User user = userRepository.findByUsername(jobUpdateDTO.getUsername())
				.orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다"));
			user.updatePrefJob(jobUpdateDTO.getJob());
			userRepository.save(user);
			return "근무 희망 직종이 변경되었습니다";
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 근무 희망 직종 변경에 실패하였습니다 : " + e.getMessage());
		}

	}

	/**
	 * 비밀번호 변경
	 *
	 * @param passwordUpdateDTO username : 사용자 ID, password : 변경할 비밀번호
	 * @return 변경 성공 여부 메시지
	 * @throws UserNotFoundException    사용자를 찾을 수 없을 때
	 * @throws RequestFailedException   비밀번호 변경에 실패했을 때
	 * @throws IllegalArgumentException 비밀번호가 입력되지 않았을 때
	 * @author 근엽
	 */
	@Override
	@Transactional
	public String updatePassword(PasswordUpdateDTO passwordUpdateDTO) {
		try {
			if (passwordUpdateDTO.getPassword() == null) {
				throw new IllegalArgumentException("비밀번호가 입력되지 않았습니다");
			}
			User user = userRepository.findByUsername(passwordUpdateDTO.getUsername())
				.orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다"));
			user.updatePassword(passwordEncoder.encode(passwordUpdateDTO.getPassword()));
			userRepository.save(user);
			return "비밀번호가 변경되었습니다";
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 비밀번호 변경에 실패하였습니다 : " + e.getMessage());
		}
	}

	/**
	 * 사용자가 요청한 상담 목록 조회
	 *
	 * @return 상담 목록
	 * @throws UserNotFoundException 사용자를 찾을 수 없을 때
	 * @author Jihwan
	 * @see CounselingResponseDTO
	 */
	@Override
	@Transactional(readOnly = true)
	public List<CounselingResponseDTO> getCounselingList() {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다"));
			List<Counseling> counselingList = counselingRepository.findAllByUserOrderBySentDate(user);
			return counselingList.stream().map(CounselingResponseDTO::new).collect(Collectors.toList());
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 상담 목록 조회에 실패하였습니다 : " + e.getMessage());
		}
	}

	/**
	 * 사용자가 작성한 취업 성공사례 게시글 목록 조회
	 *
	 * @return 사용자가 작성한 취업 성공사례 게시글 목록
	 * @throws UserNotFoundException 사용자를 찾을 수 없을 때
	 * @author Jihwan
	 * @see WrittenSuccessCaseDTO
	 */
	@Override
	@Transactional(readOnly = true)
	public List<WrittenSuccessCaseDTO> getAllSuccessList() {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다"));
			return successCaseRepository.findAllByUserOrderByCreatedDateDesc(user).stream()
				.map(WrittenSuccessCaseDTO::new)
				.collect(Collectors.toList());
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 취업 성공사례 게시글 목록 조회에 실패하였습니다 : " + e.getMessage());
		}
	}

	/**
	 * 사용자 정보 조회
	 *
	 * @param username 사용자 계정명
	 * @return 사용자 정보
	 * @throws UserNotFoundException  사용자를 찾을 수 없을 때
	 * @throws RequestFailedException 사용자 정보 조회에 실패했을 때
	 * @author Jihwan
	 */
	@Override
	@Transactional(readOnly = true)
	public UserInfoDTO getUserInfo(String username) {
		try {
			User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다"));
			return UserInfoDTO.builder()
				.username(user.getUsername())
				.prefLocation(user.getPrefLocation())
				.prefJob(user.getPrefJob())
				.build();
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 사용자 정보 조회에 실패하였습니다 : " + e.getMessage());
		}
	}

	/**
	 * 사용자 비활성화
	 *
	 * @param username 사용자 계정명
	 * @return 비활성화 성공 여부 메시지
	 * @throws UserNotFoundException  사용자를 찾을 수 없을 때
	 * @throws RequestFailedException 사용자 비활성화에 실패했을 때
	 * @author Jihwan
	 */
	@Override
	public String deactivateUser(String username) {
		try {
			User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다"));
			user.updateIsActive(false);
			userRepository.save(user);
			return "사용자(" + user.getUsername() + ")가 비활성화되었습니다";
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 사용자 비활성화에 실패하였습니다 : " + e.getMessage());
		}
	}

}
