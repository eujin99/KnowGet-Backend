package com.knowget.knowgetbackend.domain.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.knowget.knowgetbackend.domain.user.SignInResponse;
import com.knowget.knowgetbackend.domain.user.dto.UserSignInDTO;
import com.knowget.knowgetbackend.domain.user.dto.UserSignUpDTO;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.config.security.TokenProvider;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.exception.RequestFailedException;

class UserServiceImplTest {
	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private TokenProvider tokenProvider;

	@InjectMocks
	private UserServiceImpl userService;

	private User user;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);

		user = User.builder()
			.username("testuser")
			.password("encodedPassword")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.build();
	}

	@Test
	@DisplayName("사용자 이름 중복 체크 테스트 - checkUsername")
	public void testCheckUsername() {
		when(userRepository.checkUsername("testuser")).thenReturn(false);

		boolean isUnique = userService.checkUsername("testuser");

		assertThat(isUnique).isFalse();
	}

	@Test
	@DisplayName("회원가입 테스트 - register")
	public void testRegister() {
		UserSignUpDTO signUpDTO = new UserSignUpDTO();
		signUpDTO.setUsername("testuser");
		signUpDTO.setPassword("password");
		signUpDTO.setPrefLocation("Seoul");
		signUpDTO.setPrefJob("Engineer");

		when(passwordEncoder.encode(signUpDTO.getPassword())).thenReturn("encodedPassword");

		User savedUser = User.builder()
			.username(signUpDTO.getUsername())
			.password("encodedPassword")
			.prefLocation(signUpDTO.getPrefLocation())
			.prefJob(signUpDTO.getPrefJob())
			.build();

		when(userRepository.save(savedUser)).thenReturn(user);

		String result = userService.register(signUpDTO);

		assertThat(result).isEqualTo("testuser님 가입을 환영합니다.");
	}

	@Test
	@DisplayName("로그인 테스트 - login")
	public void testLogin() {
		UserSignInDTO signInDTO = new UserSignInDTO();
		signInDTO.setUsername("testuser");
		signInDTO.setPassword("password");

		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(signInDTO.getPassword(), user.getPassword())).thenReturn(true);
		when(tokenProvider.createToken("testuser:User")).thenReturn("mockToken");

		SignInResponse response = userService.login(signInDTO);

		assertThat(response.username()).isEqualTo("testuser");
		assertThat(response.role()).isEqualTo("User");
		assertThat(response.token()).isEqualTo("mockToken");
	}

	@Test
	@DisplayName("로그인 실패 테스트 - login (비밀번호 불일치)")
	public void testLoginInvalidPassword() {
		UserSignInDTO signInDTO = new UserSignInDTO();
		signInDTO.setUsername("testuser");
		signInDTO.setPassword("wrongpassword");

		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(signInDTO.getPassword(), user.getPassword())).thenReturn(false);

		assertThrows(RequestFailedException.class, () -> {
			userService.login(signInDTO);
		});
	}

	@Test
	@DisplayName("로그인 실패 테스트 - login (사용자 없음)")
	public void testLoginUserNotFound() {
		UserSignInDTO signInDTO = new UserSignInDTO();
		signInDTO.setUsername("nonexistentuser");
		signInDTO.setPassword("password");

		when(userRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

		assertThrows(RequestFailedException.class, () -> {
			userService.login(signInDTO);
		});
	}
}