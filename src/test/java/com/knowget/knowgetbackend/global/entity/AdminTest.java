package com.knowget.knowgetbackend.global.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AdminTest {

	@Test
	@DisplayName("Admin 엔티티 생성 테스트")
	public void testCreateAdmin() {
		// Given
		String username = "admin";
		String password = "password";

		// When
		User admin = User.builder()
			.username(username)
			.password(password)
			.prefLocation("NULL")
			.prefJob("NULL")
			.role("ADMIN")
			.build();

		// Then
		assertThat(admin.getUsername()).isEqualTo(username);
		assertThat(admin.getPassword()).isEqualTo(password);
	}

	@Test
	@DisplayName("Admin 엔티티 기본 생성자 테스트")
	public void testDefaultConstructor() {
		// When
		User admin = new User();

		// Then
		assertThat(admin).isNotNull();
	}
}