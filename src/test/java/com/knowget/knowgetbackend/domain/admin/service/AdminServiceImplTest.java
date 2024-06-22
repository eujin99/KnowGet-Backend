package com.knowget.knowgetbackend.domain.admin.service;

import static org.assertj.core.api.Assertions.*;
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

import com.knowget.knowgetbackend.domain.admin.dto.AdminResponseDTO;
import com.knowget.knowgetbackend.domain.admin.repository.AdminRepository;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.exception.UserNotFoundException;

class AdminServiceImplTest {
	@Mock
	private AdminRepository adminRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private AdminServiceImpl adminService;

	private User user;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		user = User.builder()
			.username("user")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Developer")
			.build();

	}

	@Test
	@DisplayName("회원 목록 조회 테스트 - 성공")
	void testGetAllUsers() {
		when(userRepository.findAll()).thenReturn(List.of(user));

		List<AdminResponseDTO> users = adminService.getAllUsers();

		assertThat(users).isNotEmpty();
		assertThat(users).hasSize(1);
		verify(userRepository, times(1)).findAll();
	}

	@Test
	@DisplayName("회원 상태 변경 테스트 - 성공 (활성화)")
	void testUpdateIsActive_Activate() {
		when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

		String result = adminService.updateIsActive(1, true);

		assertThat(result).isEqualTo("회원이 활성화되었습니다.");
		verify(userRepository, times(1)).findById(anyInt());
	}

	@Test
	@DisplayName("회원 상태 변경 테스트 - 성공 (비활성화)")
	void testUpdateIsActive_Deactivate() {
		when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

		String result = adminService.updateIsActive(1, false);

		assertThat(result).isEqualTo("회원이 비활성화되었습니다.");
		verify(userRepository, times(1)).findById(anyInt());
	}

	@Test
	@DisplayName("회원 상태 변경 테스트 - 실패 (사용자 없음)")
	void testUpdateIsActive_UserNotFound() {
		when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> adminService.updateIsActive(1, true))
			.isInstanceOf(UserNotFoundException.class)
			.hasMessageContaining("존재하지 않는 사용자입니다.");
		verify(userRepository, times(1)).findById(anyInt());
	}
}