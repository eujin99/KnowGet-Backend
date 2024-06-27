package com.knowget.knowgetbackend.domain.counseling.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.knowget.knowgetbackend.domain.counseling.dto.CounselingRequestDTO;
import com.knowget.knowgetbackend.domain.counseling.dto.CounselingResponseDTO;
import com.knowget.knowgetbackend.domain.counseling.repository.CounselingRepository;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.Counseling;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.exception.CounselingNotFoundException;
import com.knowget.knowgetbackend.global.exception.UserNotFoundException;

public class CounselingServiceImplTest {
	@Mock
	private CounselingRepository counselingRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private CounselingServiceImpl counselingService;

	private User user;
	private Counseling counseling;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Developer")
			.build();

		counseling = Counseling.builder()
			.user(user)
			.category("Career")
			.content("Need career advice")
			.build();
	}

	@Test
	@DisplayName("최신 순으로 상담 목록 조회 테스트")
	void testGetAllCounseling() {
		when(counselingRepository.findAllByOrderBySentDateDesc()).thenReturn(Arrays.asList(counseling));

		List<CounselingResponseDTO> response = counselingService.getAllCounseling();

		assertThat(response).hasSize(1);
		assertThat(response.get(0).getCategory()).isEqualTo("Career");
		assertThat(response.get(0).getContent()).isEqualTo("Need career advice");
		assertThat(response.get(0).getUser()).isEqualTo("testuser");
	}

	@Test
	@DisplayName("상담 상세 조회 테스트")
	void testGetCounselingById() {
		when(counselingRepository.findById(anyInt())).thenReturn(Optional.of(counseling));

		CounselingResponseDTO response = counselingService.getCounselingById(1);

		assertThat(response.getCategory()).isEqualTo("Career");
		assertThat(response.getContent()).isEqualTo("Need career advice");
		assertThat(response.getUser()).isEqualTo("testuser");
	}

	@Test
	@DisplayName("상담 상세 조회 실패 테스트 - 상담 없음")
	void testGetCounselingByIdNotFound() {
		when(counselingRepository.findById(anyInt())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> counselingService.getCounselingById(1))
			.isInstanceOf(CounselingNotFoundException.class)
			.hasMessageContaining("[Error] 해당 상담을 찾을 수 없습니다.");
	}

	@Test
	@DisplayName("상담 작성 테스트")
	void testSaveCounseling() {
		CounselingRequestDTO requestDTO = new CounselingRequestDTO();
		requestDTO.setUsername("testuser");
		requestDTO.setCategory("Career");
		requestDTO.setContent("Need career advice");

		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(counselingRepository.save(any(Counseling.class))).thenReturn(counseling);

		String response = counselingService.saveCounseling(requestDTO);

		assertThat(response).isEqualTo("상담이 저장되었습니다.");
		verify(counselingRepository, times(1)).save(any(Counseling.class));
	}

	@Test
	@DisplayName("상담 작성 실패 테스트 - 사용자 없음")
	void testSaveCounselingUserNotFound() {
		CounselingRequestDTO requestDTO = new CounselingRequestDTO();
		requestDTO.setUsername("testuser");
		requestDTO.setCategory("Career");
		requestDTO.setContent("Need career advice");

		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> counselingService.saveCounseling(requestDTO))
			.isInstanceOf(UserNotFoundException.class)
			.hasMessageContaining("[Error] 해당 사용자를 찾을 수 없습니다.");
	}
}