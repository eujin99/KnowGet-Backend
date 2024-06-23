package com.knowget.knowgetbackend.domain.user.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.knowget.knowgetbackend.global.entity.User;

@DataJpaTest
class UserRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserRepository userRepository;

	private User user1;
	private User user2;

	@BeforeEach
	@DisplayName("데이터 초기화")
	void setUp() {
		user1 = User.builder()
			.username("testuser1")
			.password("password1")
			.prefLocation("Seoul")
			.prefJob("Developer")
			.role("USER")
			.build();

		user2 = User.builder()
			.username("testuser2")
			.password("password2")
			.prefLocation("Busan")
			.prefJob("Designer")
			.role("ADMIN")
			.build();

		entityManager.persist(user1);
		entityManager.persist(user2);
	}

	@Test
	@DisplayName("checkUsername - 사용자 이름이 존재하지 않을 경우 true 반환")
	void checkUsername_shouldReturnTrueWhenUsernameDoesNotExist() {
		boolean result = userRepository.checkUsername("nonexistentuser");

		assertTrue(result);
	}

	@Test
	@DisplayName("checkUsername - 사용자 이름이 존재할 경우 false 반환")
	void checkUsername_shouldReturnFalseWhenUsernameExists() {
		boolean result = userRepository.checkUsername("testuser1");

		assertFalse(result);
	}

	@Test
	@DisplayName("findByUsername - 사용자 이름으로 사용자 찾기")
	void findByUsername_shouldReturnUserWhenUsernameExists() {
		Optional<User> foundUser = userRepository.findByUsername("testuser1");

		assertTrue(foundUser.isPresent());
		assertEquals("testuser1", foundUser.get().getUsername());
	}

	@Test
	@DisplayName("findByUsername - 사용자 이름이 존재하지 않을 경우 빈 Optional 반환")
	void findByUsername_shouldReturnEmptyOptionalWhenUsernameDoesNotExist() {
		Optional<User> foundUser = userRepository.findByUsername("nonexistentuser");

		assertFalse(foundUser.isPresent());
	}

	@Test
	@DisplayName("findByPrefLocationOrPrefJob - 선호 위치나 직업으로 사용자 찾기")
	void findByPrefLocationOrPrefJob_shouldReturnUsersWhenPrefLocationOrPrefJobMatches() {
		List<User> foundUsers = userRepository.findByPrefLocationOrPrefJob("Seoul", "Developer");

		assertNotNull(foundUsers);
		assertEquals(1, foundUsers.size());
		assertEquals("testuser1", foundUsers.get(0).getUsername());
	}

	@Test
	@DisplayName("findByPrefLocationOrPrefJob - 선호 위치나 직업이 일치하지 않을 경우 빈 리스트 반환")
	void findByPrefLocationOrPrefJob_shouldReturnEmptyListWhenNoPrefLocationOrPrefJobMatches() {
		List<User> foundUsers = userRepository.findByPrefLocationOrPrefJob("Daegu", "Manager");

		assertNotNull(foundUsers);
		assertTrue(foundUsers.isEmpty());
	}
}