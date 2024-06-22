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
		Admin admin = Admin.builder()
			.username(username)
			.password(password)
			.build();

		// Then
		assertThat(admin.getUsername()).isEqualTo(username);
		assertThat(admin.getPassword()).isEqualTo(password);
	}

	@Test
	@DisplayName("Admin 엔티티 기본 생성자 테스트")
	public void testDefaultConstructor() {
		// When
		Admin admin = new Admin();

		// Then
		assertThat(admin).isNotNull();
	}
}