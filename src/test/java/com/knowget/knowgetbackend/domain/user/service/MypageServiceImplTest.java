package com.knowget.knowgetbackend.domain.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.knowget.knowgetbackend.domain.bookmark.repository.BookmarkRepository;
import com.knowget.knowgetbackend.domain.counseling.dto.CounselingResponseDTO;
import com.knowget.knowgetbackend.domain.counseling.repository.CounselingRepository;
import com.knowget.knowgetbackend.domain.successCase.repository.SuccessCaseRepository;
import com.knowget.knowgetbackend.domain.user.dto.JobUpdateDTO;
import com.knowget.knowgetbackend.domain.user.dto.LocationUpdateDTO;
import com.knowget.knowgetbackend.domain.user.dto.PasswordUpdateDTO;
import com.knowget.knowgetbackend.domain.user.dto.UserInfoDTO;
import com.knowget.knowgetbackend.domain.user.dto.WrittenSuccessCaseDTO;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.exception.RequestFailedException;

class MypageServiceImplTest {
	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private SuccessCaseRepository successCaseRepository;

	@Mock
	private CounselingRepository counselingRepository;

	@Mock
	private BookmarkRepository bookmarkRepository;

	@InjectMocks
	private MypageServiceImpl mypageService;

	private User user;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.build();

		// Set up mock SecurityContext
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(authentication.getName()).thenReturn("testuser");
		SecurityContextHolder.setContext(securityContext);
	}

	@Test
	@DisplayName("사용자의 근무 희망 직종을 변경 테스트 - updatePrefJob")
	void testUpdatePrefJob() {
		JobUpdateDTO jobUpdateDTO = new JobUpdateDTO("Doctor", "testuser");
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

		String result = mypageService.updatePrefJob(jobUpdateDTO);

		assertThat(result).isEqualTo("근무 희망 직종이 변경되었습니다");
		assertThat(user.getPrefJob()).isEqualTo("Doctor");
		verify(userRepository, times(1)).findByUsername(anyString());
	}

	@Test
	@DisplayName("존재하지 않는 사용자로 인해 근무 희망 직종 변경 실패 테스트 - updatePrefJob")
	void testUpdatePrefJobUserNotFound() {
		JobUpdateDTO jobUpdateDTO = new JobUpdateDTO("Doctor", "unknownuser");
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

		assertThrows(RequestFailedException.class, () -> mypageService.updatePrefJob(jobUpdateDTO));
	}

	@Test
	@DisplayName("사용자의 근무 희망 지역을 변경 테스트 - updatePrefLocation")
	void testUpdatePrefLocation() {
		LocationUpdateDTO locationUpdateDTO = new LocationUpdateDTO("testuser", "Busan");
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

		String result = mypageService.updatePrefLocation(locationUpdateDTO);

		assertThat(result).isEqualTo("근무 희망 지역이 변경되었습니다");
		assertThat(user.getPrefLocation()).isEqualTo("Busan");
		verify(userRepository, times(1)).findByUsername(anyString());
	}

	@Test
	@DisplayName("존재하지 않는 사용자로 인해 근무 희망 지역 변경 실패 테스트 - updatePrefLocation")
	void testUpdatePrefLocationUserNotFound() {
		LocationUpdateDTO locationUpdateDTO = new LocationUpdateDTO("unknownuser", "Busan");
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

		assertThrows(RequestFailedException.class, () -> mypageService.updatePrefLocation(locationUpdateDTO));
	}

	@Test
	@DisplayName("사용자 비밀번호 변경 테스트 - updatePassword")
	void testUpdatePassword() {
		PasswordUpdateDTO passwordUpdateDTO = new PasswordUpdateDTO("testuser", "newpassword");
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

		String result = mypageService.updatePassword(passwordUpdateDTO);

		assertThat(result).isEqualTo("비밀번호가 변경되었습니다");
		assertThat(user.getPassword()).isEqualTo("encodedPassword");
		verify(userRepository, times(1)).findByUsername(anyString());
		verify(passwordEncoder, times(1)).encode(anyString());
	}

	@Test
	@DisplayName("존재하지 않는 사용자로 인해 비밀번호 변경 실패 테스트 - updatePassword")
	void testUpdatePasswordUserNotFound() {
		PasswordUpdateDTO passwordUpdateDTO = new PasswordUpdateDTO("unknownuser", "newpassword");
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

		assertThrows(RequestFailedException.class, () -> mypageService.updatePassword(passwordUpdateDTO));
	}

	@Test
	@DisplayName("비밀번호 미입력으로 인한 비밀번호 변경 실패 테스트 - updatePassword")
	void testUpdatePasswordNoPassword() {
		PasswordUpdateDTO passwordUpdateDTO = new PasswordUpdateDTO("testuser", null);

		assertThrows(RequestFailedException.class, () -> mypageService.updatePassword(passwordUpdateDTO));
	}

	@Test
	@DisplayName("사용자가 요청한 상담 목록 조회 테스트 - getCounselingList")
	void testGetCounselingList() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		// 필요한 경우 더미 데이터 추가 및 Mock 설정

		List<CounselingResponseDTO> counselingList = mypageService.getCounselingList();

		// 필요한 경우 검증 로직 추가
	}

	@Test
	@DisplayName("사용자가 작성한 취업 성공사례 게시글 목록 조회 테스트 - getAllSuccessList")
	void testGetAllSuccessList() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		// 필요한 경우 더미 데이터 추가 및 Mock 설정

		List<WrittenSuccessCaseDTO> successList = mypageService.getAllSuccessList();

		// 필요한 경우 검증 로직 추가
	}

	@Test
	@DisplayName("사용자 정보 조회 테스트 - getUserInfo")
	void testGetUserInfo() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

		UserInfoDTO userInfo = mypageService.getUserInfo("testuser");

		assertThat(userInfo.getUsername()).isEqualTo("testuser");
		assertThat(userInfo.getPrefLocation()).isEqualTo("Seoul");
		assertThat(userInfo.getPrefJob()).isEqualTo("Engineer");
		verify(userRepository, times(1)).findByUsername(anyString());
	}

	@Test
	@DisplayName("존재하지 않는 사용자로 인한 사용자 정보 조회 실패 테스트 - getUserInfo")
	void testGetUserInfoUserNotFound() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

		assertThrows(RequestFailedException.class, () -> mypageService.getUserInfo("unknownuser"));
	}
}