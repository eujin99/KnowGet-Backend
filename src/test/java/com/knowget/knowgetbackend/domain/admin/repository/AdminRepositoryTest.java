package com.knowget.knowgetbackend.domain.admin.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.knowget.knowgetbackend.global.entity.Admin;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class AdminRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private AdminRepository adminRepository;

	private Admin admin;

	@BeforeEach
	public void setUp() {
		admin = Admin.builder()
			.username("adminuser")
			.password("password")
			.build();
		entityManager.persist(admin);
		entityManager.flush();
	}

	@Test
	@DisplayName("Admin 엔티티 조회 테스트 - findByUsername")
	public void testFindByUsername() {
		// When
		Admin foundAdmin = adminRepository.findByUsername(admin.getUsername());

		// Then
		assertThat(foundAdmin).isNotNull();
		assertThat(foundAdmin.getUsername()).isEqualTo(admin.getUsername());
		assertThat(foundAdmin.getPassword()).isEqualTo(admin.getPassword());
	}

	@Test
	@DisplayName("존재하지 않는 Admin 조회 테스트 - findByUsername")
	public void testFindByUsernameNotFound() {
		// When
		Admin foundAdmin = adminRepository.findByUsername("nonexistent");

		// Then
		assertThat(foundAdmin).isNull();
	}
}