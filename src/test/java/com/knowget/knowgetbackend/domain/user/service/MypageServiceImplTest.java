package com.knowget.knowgetbackend.domain.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyString;
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
import com.knowget.knowgetbackend.global.entity.Post;
import com.knowget.knowgetbackend.global.entity.SuccessCase;
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

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		// Mock SecurityContext
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getName()).thenReturn("testuser");
	}

	@Test
	@DisplayName("사용자가 북마크한 공고 목록 조회 테스트")
	void testGetBookmarkList() {
		// Given
		User user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.role("USER")
			.build();

		Post post = Post.builder()
			.joReqstNo("REQ123")
			.joRegistNo("REG123")
			.cmpnyNm("CompanyName")
			.bsnsSumryCn("Business Summary")
			.rcritJssfcCmmnCodeSe("RCR123")
			.jobcodeNm("JobCode")
			.rcritNmprCo(10)
			.acdmcrCmmnCodeSe("ACD123")
			.acdmcrNm("Academic Name")
			.emplymStleCmmnCodeSe("EMP123")
			.emplymStleCmmnMm("EMP MM")
			.workPararBassAdresCn("Address")
			.subwayNm("SubwayName")
			.dtyCn("Duty Content")
			.careerCndCmmnCodeSe("CAR123")
			.careerCndNm("Career Name")
			.hopeWage("1000000")
			.retGrantsNm("Retirement Grants")
			.workTimeNm("Work Time")
			.workTmNm("Work TM")
			.holidayNm("Holiday")
			.weekWorkHr("40")
			.joFeinsrSbscrbNm("Insurance")
			.rceptClosNm("Receipt Close")
			.rceptMthIemNm("Receipt Method Item")
			.modelMthNm("Model Method")
			.rceptMthNm("Receipt Method")
			.presentnPapersNm("Presentation Papers")
			.mngrNm("Manager Name")
			.mngrPhonNo("Manager Phone")
			.mngrInsttNm("Manager Institute")
			.bassAdresCn("Base Address")
			.joSj("Job Subject")
			.joRegDt("2023-01-01")
			.guiLn("Guide Line")
			.gu("GU")
			.jobCode("Job Code")
			.build();

		Bookmark bookmark = Bookmark.builder()
			.user(user)
			.post(post)
			.build();

		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(bookmarkRepository.findByUserAndIsBookmarkedTrue(any())).thenReturn(List.of(bookmark));

		// When
		List<PostResponseDTO> bookmarkList = mypageService.getBookmarkList();

		// Then
		assertThat(bookmarkList).isNotNull();
		assertThat(bookmarkList).hasSize(1);
		assertThat(bookmarkList.get(0).getJoReqstNo()).isEqualTo("REQ123");
		verify(userRepository, times(1)).findByUsername(anyString());
		verify(bookmarkRepository, times(1)).findByUserAndIsBookmarkedTrue(any());
	}

	@Test
	@DisplayName("사용자의 근무 희망 지역 변경 테스트")
	void testUpdatePrefLocation() {
		// Given
		User user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.role("USER")
			.build();

		LocationUpdateDTO locationUpdateDTO = new LocationUpdateDTO("testuser", "Busan");
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

		// When
		String result = mypageService.updatePrefLocation(locationUpdateDTO);

		// Then
		assertThat(result).isEqualTo("근무 희망 지역이 변경되었습니다");
		verify(userRepository, times(1)).findByUsername(anyString());
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	@DisplayName("사용자의 근무 희망 직종 변경 테스트")
	void testUpdatePrefJob() {
		// Given
		User user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.role("USER")
			.build();

		JobUpdateDTO jobUpdateDTO = new JobUpdateDTO("Developer", "testuser");
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

		// When
		String result = mypageService.updatePrefJob(jobUpdateDTO);

		// Then
		assertThat(result).isEqualTo("근무 희망 직종이 변경되었습니다");
		verify(userRepository, times(1)).findByUsername(anyString());
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	@DisplayName("비밀번호 변경 테스트")
	void testUpdatePassword() {
		// Given
		User user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.role("USER")
			.build();

		PasswordUpdateDTO passwordUpdateDTO = new PasswordUpdateDTO("testuser", "newpassword");
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(passwordEncoder.encode(anyString())).thenReturn("encodedpassword");

		// When
		String result = mypageService.updatePassword(passwordUpdateDTO);

		// Then
		assertThat(result).isEqualTo("비밀번호가 변경되었습니다");
		verify(userRepository, times(1)).findByUsername(anyString());
		verify(userRepository, times(1)).save(any(User.class));
		verify(passwordEncoder, times(1)).encode(anyString());
	}

	@Test
	@DisplayName("비밀번호 변경 실패 테스트 - 비밀번호가 입력되지 않았을 때")
	void testUpdatePasswordFailure() {
		// Given
		PasswordUpdateDTO passwordUpdateDTO = new PasswordUpdateDTO("testuser", null);

		// When & Then
		RequestFailedException exception = assertThrows(RequestFailedException.class,
			() -> mypageService.updatePassword(passwordUpdateDTO));
		assertThat(exception.getMessage()).contains("비밀번호가 입력되지 않았습니다");
	}

	@Test
	@DisplayName("사용자가 요청한 상담 목록 조회 테스트")
	void testGetCounselingList() {
		// Given
		User user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.role("USER")
			.build();

		Counseling counseling = Counseling.builder()
			.user(user)
			.category("Category")
			.content("Test counseling")
			.build();

		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(counselingRepository.findAllByUserOrderBySentDate(any())).thenReturn(List.of(counseling));

		// When
		List<CounselingResponseDTO> counselingList = mypageService.getCounselingList();

		// Then
		assertThat(counselingList).isNotNull();
		assertThat(counselingList).hasSize(1);
		assertThat(counselingList.get(0).getContent()).isEqualTo("Test counseling");
		verify(userRepository, times(1)).findByUsername(anyString());
		verify(counselingRepository, times(1)).findAllByUserOrderBySentDate(any());
	}

	@Test
	@DisplayName("사용자가 작성한 취업 성공사례 게시글 목록 조회 테스트")
	void testGetAllSuccessList() {
		// Given
		User user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.role("USER")
			.build();

		SuccessCase successCase = SuccessCase.builder()
			.user(user)
			.title("Success Story")
			.content("This is a success story.")
			.build();

		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(successCaseRepository.findAllByUserOrderByCreatedDateDesc(any())).thenReturn(List.of(successCase));

		// When
		List<WrittenSuccessCaseDTO> successList = mypageService.getAllSuccessList();

		// Then
		assertThat(successList).isNotNull();
		assertThat(successList).hasSize(1);
		assertThat(successList.get(0).getTitle()).isEqualTo("Success Story");
		verify(userRepository, times(1)).findByUsername(anyString());
		verify(successCaseRepository, times(1)).findAllByUserOrderByCreatedDateDesc(any());
	}

	@Test
	@DisplayName("사용자 정보 조회 테스트")
	void testGetUserInfo() {
		// Given
		User user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.role("USER")
			.build();

		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

		// When
		UserInfoDTO userInfo = mypageService.getUserInfo("testuser");

		// Then
		assertThat(userInfo).isNotNull();
		assertThat(userInfo.getUsername()).isEqualTo("testuser");
		assertThat(userInfo.getPrefLocation()).isEqualTo("Seoul");
		assertThat(userInfo.getPrefJob()).isEqualTo("Engineer");
		verify(userRepository, times(1)).findByUsername(anyString());
	}

	@Test
	@DisplayName("사용자 비활성화 테스트 - 성공 시나리오")
	void testDeactivateUserSuccess() {
		// Given
		User user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.role("USER")
			.build();

		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

		// When
		String result = mypageService.deactivateUser("testuser");

		// Then
		assertThat(result).isEqualTo("사용자(testuser)가 비활성화되었습니다");
		verify(userRepository, times(1)).findByUsername(anyString());
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	@DisplayName("사용자 비활성화 테스트 - 실패 시나리오")
	void testDeactivateUserFailure() {
		// Given
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

		// When & Then
		RequestFailedException exception = assertThrows(RequestFailedException.class,
			() -> mypageService.deactivateUser("nonexistentuser"));
		assertThat(exception.getMessage()).contains("존재하지 않는 사용자입니다");
		verify(userRepository, times(1)).findByUsername(anyString());
		verify(userRepository, times(0)).save(any(User.class));
	}
}