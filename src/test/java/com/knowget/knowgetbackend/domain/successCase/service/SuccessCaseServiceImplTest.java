package com.knowget.knowgetbackend.domain.successCase.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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

import com.knowget.knowgetbackend.domain.successCase.dto.SuccessCaseRequestDTO;
import com.knowget.knowgetbackend.domain.successCase.dto.SuccessCaseResponseDTO;
import com.knowget.knowgetbackend.domain.successCase.repository.SuccessCaseRepository;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.dto.ResultMessageDTO;
import com.knowget.knowgetbackend.global.entity.SuccessCase;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.exception.RequestFailedException;
import com.knowget.knowgetbackend.global.exception.ResourceNotFoundException;

class SuccessCaseServiceImplTest {
	@Mock
	private SuccessCaseRepository successCaseRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private SuccessCaseServiceImpl successCaseService;

	@Mock
	private SecurityContext securityContext;

	@Mock
	private Authentication authentication;

	private User user;
	private SuccessCase successCase;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		SecurityContextHolder.setContext(securityContext);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(authentication.getName()).thenReturn("testuser");

		user = User.builder().username("testuser").build();
		successCase = SuccessCase.builder().title("Test Title").content("Test Content").user(user).build();
	}

	@Test
	@DisplayName("특정 SuccessCase 조회 테스트 - getSuccessCase")
	void testGetSuccessCase() {
		when(successCaseRepository.findById(1)).thenReturn(Optional.of(successCase));

		SuccessCaseResponseDTO result = successCaseService.getSuccessCase(1);

		assertThat(result.getTitle()).isEqualTo("Test Title");
		assertThat(result.getContent()).isEqualTo("Test Content");
		verify(successCaseRepository, times(1)).findById(1);
	}

	@Test
	@DisplayName("존재하지 않는 SuccessCase 조회 테스트 - getSuccessCase")
	void testGetSuccessCaseNotFound() {
		when(successCaseRepository.findById(1)).thenReturn(Optional.empty());

		assertThrows(RequestFailedException.class, () -> successCaseService.getSuccessCase(1));
	}

	@Test
	@DisplayName("전체 SuccessCase 목록 조회 테스트 - getAllSuccessCases")
	void testGetAllSuccessCases() {
		when(successCaseRepository.findAll()).thenReturn(List.of(successCase));

		List<SuccessCaseResponseDTO> result = successCaseService.getAllSuccessCases();

		assertThat(result).hasSize(1);
		verify(successCaseRepository, times(1)).findAll();
	}

	@Test
	@DisplayName("존재하지 않는 SuccessCase 목록 조회 테스트 - getAllSuccessCases")
	void testGetAllSuccessCasesNotFound() {
		when(successCaseRepository.findAll()).thenReturn(List.of());

		assertThrows(ResourceNotFoundException.class, () -> successCaseService.getAllSuccessCases());
	}

	@Test
	@DisplayName("SuccessCase 생성 테스트 - createSuccessCase")
	void testCreateSuccessCase() {
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
		SuccessCaseRequestDTO requestDTO = new SuccessCaseRequestDTO("New Title", "New Content", "testuser");

		ResultMessageDTO result = successCaseService.createSuccessCase(requestDTO);

		assertThat(result.getMessage()).isEqualTo("SuccessCase가 성공적으로 생성되었습니다");
		verify(successCaseRepository, times(1)).save(any(SuccessCase.class));
		verify(userRepository, times(1)).findByUsername("testuser");
	}

	@Test
	@DisplayName("SuccessCase 생성 실패 테스트 - 사용자 미존재")
	void testCreateSuccessCaseUserNotFound() {
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
		SuccessCaseRequestDTO requestDTO = new SuccessCaseRequestDTO("New Title", "New Content", "testuser");

		assertThrows(RequestFailedException.class, () -> successCaseService.createSuccessCase(requestDTO));
	}

	@Test
	@DisplayName("SuccessCase 삭제 테스트 - deleteSuccessCase")
	void testDeleteSuccessCase() {
		when(successCaseRepository.findById(1)).thenReturn(Optional.of(successCase));

		ResultMessageDTO result = successCaseService.deleteSuccessCase(1);

		assertThat(result.getMessage()).isEqualTo("해당 글이 삭제 되었습니다");
		verify(successCaseRepository, times(1)).delete(successCase);
		verify(successCaseRepository, times(1)).findById(1);
	}

	@Test
	@DisplayName("SuccessCase 삭제 실패 테스트 - 게시글 미존재")
	void testDeleteSuccessCaseNotFound() {
		when(successCaseRepository.findById(1)).thenReturn(Optional.empty());

		assertThrows(RequestFailedException.class, () -> successCaseService.deleteSuccessCase(1));
	}

	@Test
	@DisplayName("SuccessCase 검색 테스트 - searchSuccessCase")
	void testSearchSuccessCase() {
		when(successCaseRepository.findByTitleContaining("Test")).thenReturn(List.of(successCase));

		List<SuccessCaseResponseDTO> result = successCaseService.searchSuccessCase("Test");

		assertThat(result).hasSize(1);
		verify(successCaseRepository, times(1)).findByTitleContaining("Test");
	}

	@Test
	@DisplayName("SuccessCase 검색 실패 테스트 - 검색 결과 없음")
	void testSearchSuccessCaseNotFound() {
		when(successCaseRepository.findByTitleContaining("Test")).thenReturn(List.of());

		assertThrows(ResourceNotFoundException.class, () -> successCaseService.searchSuccessCase("Test"));
	}

	// @Test
	// @DisplayName("SuccessCase 승인상태 업데이트 테스트 - updateSuccessCaseApproval")
	// void testUpdateSuccessCaseApproval() {
	// 	when(successCaseRepository.findById(1)).thenReturn(Optional.of(successCase));
	//
	// 	String result = successCaseService.updateSuccessCaseApproval(1, 1);
	//
	// 	assertThat(result).isEqualTo("1번 취업 성공사례 게시가 승인되었습니다");
	// 	verify(successCaseRepository, times(1)).findById(1);
	// }

	// @Test
	// @DisplayName("SuccessCase 승인상태 업데이트 실패 테스트 - 게시글 미존재")
	// void testUpdateSuccessCaseApprovalNotFound() {
	// 	when(successCaseRepository.findById(1)).thenReturn(Optional.empty());
	//
	// 	assertThrows(SuccessCaseNotFoundException.class, () -> successCaseService.updateSuccessCaseApproval(1, 1));
	// }
}