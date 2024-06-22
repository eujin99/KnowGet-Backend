package com.knowget.knowgetbackend.global.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

	private User user;

	@BeforeEach
	public void setUp() {
		user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.build();
	}

	@Test
	@DisplayName("User 엔티티 생성 테스트")
	public void testCreateUser() {
		// Given
		String username = "testuser";
		String password = "password";
		String prefLocation = "Seoul";
		String prefJob = "Engineer";

		// When
		User user = User.builder()
			.username(username)
			.password(password)
			.prefLocation(prefLocation)
			.prefJob(prefJob)
			.build();

		// Then
		assertThat(user.getUsername()).isEqualTo(username);
		assertThat(user.getPassword()).isEqualTo(password);
		assertThat(user.getPrefLocation()).isEqualTo(prefLocation);
		assertThat(user.getPrefJob()).isEqualTo(prefJob);
		assertThat(user.getIsActive()).isTrue();
	}

	@Test
	@DisplayName("User 엔티티 기본 생성자 테스트")
	public void testDefaultConstructor() {
		// When
		User user = new User();

		// Then
		assertThat(user).isNotNull();
	}

	@Test
	@DisplayName("User 엔티티 비밀번호 업데이트 테스트")
	public void testUpdatePassword() {
		// Given
		String newPassword = "newpassword";

		// When
		user.updatePassword(newPassword);

		// Then
		assertThat(user.getPassword()).isEqualTo(newPassword);
	}

	@Test
	@DisplayName("User 엔티티 선호 위치 업데이트 테스트")
	public void testUpdatePrefLocation() {
		// Given
		String newPrefLocation = "Busan";

		// When
		user.updatePrefLocation(newPrefLocation);

		// Then
		assertThat(user.getPrefLocation()).isEqualTo(newPrefLocation);
	}

	@Test
	@DisplayName("User 엔티티 선호 직업 업데이트 테스트")
	public void testUpdatePrefJob() {
		// Given
		String newPrefJob = "Doctor";

		// When
		user.updatePrefJob(newPrefJob);

		// Then
		assertThat(user.getPrefJob()).isEqualTo(newPrefJob);
	}

	@Test
	@DisplayName("User 엔티티 활성화 상태 업데이트 테스트")
	public void testUpdateIsActive() {
		// Given
		Boolean newIsActive = false;

		// When
		user.updateIsActive(newIsActive);

		// Then
		assertThat(user.getIsActive()).isEqualTo(newIsActive);
	}
}