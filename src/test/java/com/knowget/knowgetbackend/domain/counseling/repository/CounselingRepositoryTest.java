package com.knowget.knowgetbackend.domain.counseling.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.knowget.knowgetbackend.global.entity.Counseling;
import com.knowget.knowgetbackend.global.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class CounselingRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private CounselingRepository counselingRepository;

	private User user1;
	private User user2;
	private Counseling counseling1;
	private Counseling counseling2;
	private Counseling counseling3;

	@BeforeEach
	public void setUp() {
		user1 = User.builder()
			.username("user1")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.role("USER")
			.build();
		entityManager.persist(user1);

		user2 = User.builder()
			.username("user2")
			.password("password")
			.prefLocation("Busan")
			.prefJob("Teacher")
			.role("USER")
			.build();
		entityManager.persist(user2);

		counseling1 = Counseling.builder()
			.user(user1)
			.category("Career")
			.content("Career counseling content 1")
			.build();
		entityManager.persist(counseling1);

		counseling2 = Counseling.builder()
			.user(user1)
			.category("Life")
			.content("Life counseling content 2")
			.build();
		entityManager.persist(counseling2);

		counseling3 = Counseling.builder()
			.user(user2)
			.category("Health")
			.content("Health counseling content 3")
			.build();
		entityManager.persist(counseling3);

		entityManager.flush();
	}

	@Test
	@DisplayName("Counseling 엔티티를 날짜 순으로 내림차순 조회 테스트 - findAllByOrderBySentDateDesc")
	public void testFindAllByOrderBySentDateDesc() {
		// When
		List<Counseling> counselingList = counselingRepository.findAllByOrderBySentDateDesc();

		// Then
		assertThat(counselingList).hasSize(3);
		assertThat(counselingList.get(0)).isEqualTo(counseling3);
		assertThat(counselingList.get(1)).isEqualTo(counseling2);
		assertThat(counselingList.get(2)).isEqualTo(counseling1);
	}

	@Test
	@DisplayName("특정 사용자에 대한 Counseling 엔티티를 날짜 순으로 조회 테스트 - findAllByUserOrderBySentDate")
	public void testFindAllByUserOrderBySentDate() {
		// When
		List<Counseling> counselingList = counselingRepository.findAllByUserOrderBySentDate(user1);

		// Then
		assertThat(counselingList).hasSize(2);
		assertThat(counselingList.get(0)).isEqualTo(counseling1);
		assertThat(counselingList.get(1)).isEqualTo(counseling2);
	}

	@Test
	@DisplayName("특정 사용자가 없는 경우의 Counseling 엔티티 조회 테스트 - findAllByUserOrderBySentDate")
	public void testFindAllByUserOrderBySentDateNoUser() {
		// Given
		User user3 = User.builder()
			.username("user3")
			.password("password")
			.prefLocation("Daegu")
			.prefJob("Doctor")
			.role("USER")
			.build();
		entityManager.persist(user3);
		entityManager.flush();

		// When
		List<Counseling> counselingList = counselingRepository.findAllByUserOrderBySentDate(user3);

		// Then
		assertThat(counselingList).isEmpty();
	}
}