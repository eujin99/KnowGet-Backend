package com.knowget.knowgetbackend.domain.user.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.knowget.knowgetbackend.global.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserRepositoryTest {
	@Autowired
	private UserRepository userRepository;

	private User user1;
	private User user2;

	@BeforeEach
	public void setUp() {
		user1 = User.builder()
			.username("user1")
			.password("password1")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.build();

		user2 = User.builder()
			.username("user2")
			.password("password2")
			.prefLocation("Busan")
			.prefJob("Doctor")
			.build();

		userRepository.save(user1);
		userRepository.save(user2);
	}

	@Test
	@DisplayName("사용자 이름 중복 체크 테스트 - checkUsername")
	public void testCheckUsername() {
		boolean isUnique1 = userRepository.checkUsername("user1");
		boolean isUnique2 = userRepository.checkUsername("user3");

		assertThat(isUnique1).isFalse();
		assertThat(isUnique2).isTrue();
	}

	@Test
	@DisplayName("사용자 이름으로 사용자 찾기 테스트 - findByUsername")
	public void testFindByUsername() {
		Optional<User> foundUser = userRepository.findByUsername("user1");

		assertThat(foundUser).isPresent();
		assertThat(foundUser.get().getUsername()).isEqualTo("user1");
	}

	@Test
	@DisplayName("선호 위치 또는 직업으로 사용자 찾기 테스트 - findByPrefLocationOrPrefJob")
	public void testFindByPrefLocationOrPrefJob() {
		List<User> users = userRepository.findByPrefLocationOrPrefJob("Seoul", "Doctor");

		assertThat(users).hasSize(2);
		assertThat(users).contains(user1, user2);
	}
}