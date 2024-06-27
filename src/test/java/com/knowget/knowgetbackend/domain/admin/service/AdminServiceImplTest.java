package com.knowget.knowgetbackend.domain.admin.service;

import static org.junit.jupiter.api.Assertions.*;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.knowget.knowgetbackend.domain.admin.dto.AdminModifyDTO;
import com.knowget.knowgetbackend.domain.admin.dto.AdminResponseDTO;
import com.knowget.knowgetbackend.domain.admin.dto.AdminSignupDTO;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.exception.RequestFailedException;
import com.knowget.knowgetbackend.global.exception.UserNotFoundException;

class AdminServiceImplTest {
	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private AdminServiceImpl adminServiceImpl;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("모든 사용자를 조회할 때 성공적으로 결과를 반환해야 한다")
	void testGetAllUsers() {
		User user1 = User.builder()
			.username("user1")
			.role("USER")
			.prefLocation("Location1")
			.prefJob("Job1")
			.build();

		User user2 = User.builder()
			.username("user2")
			.role("USER")
			.prefLocation("Location2")
			.prefJob("Job2")
			.build();
		user2.updateIsActive(false);

		when(userRepository.findByRole("USER")).thenReturn(Arrays.asList(user1, user2));

		List<AdminResponseDTO> result = adminServiceImpl.getAllUsers();

		assertEquals(2, result.size());
		assertEquals("user1", result.get(0).getUserName());
		assertEquals("Location1", result.get(0).getPrefLocation());
		assertEquals("Job1", result.get(0).getPrefJob());
		assertTrue(result.get(0).getIsActive());

		assertEquals("user2", result.get(1).getUserName());
		assertEquals("Location2", result.get(1).getPrefLocation());
		assertEquals("Job2", result.get(1).getPrefJob());
		assertFalse(result.get(1).getIsActive());
	}

	@Test
	@DisplayName("사용자 조회 중 예외가 발생하면 RequestFailedException을 던져야 한다")
	void testGetAllUsers_Exception() {
		when(userRepository.findByRole("USER")).thenThrow(new RuntimeException("Database error"));

		RequestFailedException exception = assertThrows(RequestFailedException.class, () -> {
			adminServiceImpl.getAllUsers();
		});

		assertEquals("[Error] 회원 목록 조회에 실패했습니다 : Database error", exception.getMessage());
	}

	@Test
	@DisplayName("사용자의 활성화 상태를 변경할 때 성공적으로 변경되어야 한다")
	void testUpdateIsActive() {
		User user = User.builder()
			.username("user1")
			.role("USER")
			.build();

		when(userRepository.findById(1)).thenReturn(Optional.of(user));
		AdminModifyDTO adminModifyDTO = new AdminModifyDTO(false);

		String result = adminServiceImpl.updateIsActive(1, adminModifyDTO.getIsActive());

		assertFalse(user.getIsActive());
		assertEquals("회원이 비활성화되었습니다.", result);
		verify(userRepository).save(user);
	}

	@Test
	@DisplayName("존재하지 않는 사용자의 활성화 상태를 변경하려 할 때 UserNotFoundException을 던져야 한다")
	void testUpdateIsActive_UserNotFoundException() {
		when(userRepository.findById(1)).thenReturn(Optional.empty());
		AdminModifyDTO adminModifyDTO = new AdminModifyDTO(false);

		UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
			adminServiceImpl.updateIsActive(1, adminModifyDTO.getIsActive());
		});

		assertEquals("[Error] 존재하지 않는 사용자입니다.", exception.getMessage());
	}

	@Test
	@DisplayName("새로운 관리자를 등록할 때 성공적으로 등록되어야 한다")
	void testRegister() {
		AdminSignupDTO adminSignupDTO = new AdminSignupDTO("admin", "password", "location", "job");

		when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());
		when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

		String result = adminServiceImpl.register(adminSignupDTO);

		assertEquals("admin: 가입이 완료되었습니다", result);
		verify(userRepository).save(any(User.class));
	}

	@Test
	@DisplayName("이미 존재하는 관리자 계정을 등록하려 할 때 IllegalArgumentException을 던져야 한다")
	void testRegister_ExistingUser() {
		AdminSignupDTO adminSignupDTO = new AdminSignupDTO("admin", "password", "location", "job");

		User existingUser = User.builder()
			.username("admin")
			.password("password")
			.prefLocation("location")
			.prefJob("job")
			.role("ADMIN")
			.build();

		when(userRepository.findByUsername("admin")).thenReturn(Optional.of(existingUser));

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			adminServiceImpl.register(adminSignupDTO);
		});

		assertEquals("이미 존재하는 관리자입니다", exception.getMessage());
	}
}