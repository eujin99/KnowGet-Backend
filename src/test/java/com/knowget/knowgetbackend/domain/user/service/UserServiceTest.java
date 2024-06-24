package com.knowget.knowgetbackend.domain.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.knowget.knowgetbackend.domain.user.dto.UserSignUpDTO;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.exception.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	private UserSignUpDTO userSignUpDTO;

	@BeforeEach
	@DisplayName("Test Setup")
	void setUp() {
		userSignUpDTO = new UserSignUpDTO();
		userSignUpDTO.setUsername("testUser");
		userSignUpDTO.setPassword("testPassword");
		userSignUpDTO.setPrefLocation("Seoul");
		userSignUpDTO.setPrefJob("Developer");
	}

	@Test
	@DisplayName("사용자명 중복 확인 - 성공")
	void checkUsernameSuccess() {
		when(userRepository.checkUsername(anyString())).thenReturn(true);
		assertTrue(userService.checkUsername("testUser"));
		verify(userRepository, times(1)).checkUsername("testUser");
	}

	@Test
	@DisplayName("사용자명 중복 확인 - 실패")
	void checkUsernameFail() {
		when(userRepository.checkUsername(anyString())).thenReturn(false);
		assertFalse(userService.checkUsername("testUser"));
		verify(userRepository, times(1)).checkUsername("testUser");
	}

	@Test
	@DisplayName("사용자 회원가입 - 성공")
	void registerSuccess() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
		when(userRepository.save(any(User.class))).thenReturn(User.builder().build());

		String result = userService.register(userSignUpDTO);
		assertEquals("testUser님 가입을 환영합니다.", result);

		verify(userRepository, times(1)).findByUsername("testUser");
		verify(passwordEncoder, times(1)).encode("testPassword");
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	@DisplayName("사용자 회원가입 - 실패: 이미 존재하는 사용자")
	void registerUserAlreadyExists() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(User.builder().build()));

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			userService.register(userSignUpDTO);
		});

		assertEquals("이미 존재하는 사용자입니다", exception.getMessage());

		verify(userRepository, times(1)).findByUsername("testUser");
		verify(passwordEncoder, times(0)).encode(anyString());
		verify(userRepository, times(0)).save(any(User.class));
	}

	@Test
	@DisplayName("사용자 조회 - 성공")
	void findByUsernameSuccess() {
		when(userRepository.findByUsername(anyString())).thenReturn(
			Optional.of(User.builder().username("testUser").build()));

		User user = userService.findByUsername("testUser");
		assertEquals("testUser", user.getUsername());

		verify(userRepository, times(1)).findByUsername("testUser");
	}

	@Test
	@DisplayName("사용자 조회 - 실패: 사용자 찾을 수 없음")
	void findByUsernameNotFound() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

		UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
			userService.findByUsername("testUser");
		});

		assertEquals("testUser와(과) 일치하는 사용자를 찾을 수 없습니다", exception.getMessage());

		verify(userRepository, times(1)).findByUsername("testUser");
	}
}