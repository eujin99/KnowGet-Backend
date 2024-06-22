package com.knowget.knowgetbackend.domain.counseling.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

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

	private User testUser;
	private Counseling counseling1;
	private Counseling counseling2;

	@BeforeEach
	public void setUp() {
		testUser = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.build();
		entityManager.persist(testUser);

		counseling1 = Counseling.builder()
			.user(testUser)
			.category("Career")
			.content("This is a counseling content 1.")
			.build();
		counseling2 = Counseling.builder()
			.user(testUser)
			.category("Career")
			.content("This is a counseling content 2.")
			.build();

		entityManager.persist(counseling1);
		entityManager.persist(counseling2);
		entityManager.flush();
	}

	@Test
	@DisplayName("상담 최신순으로 조회 테스트")
	public void testFindAllByOrderBySentDateDesc() {
		// When
		List<Counseling> counselings = counselingRepository.findAllByOrderBySentDateDesc();

		// Then
		assertThat(counselings.size()).isEqualTo(2);
		assertThat(counselings.get(0)).isEqualTo(counseling2);
		assertThat(counselings.get(1)).isEqualTo(counseling1);
	}

	@Test
	@DisplayName("특정 사용자의 상담 최신순으로 조회 테스트")
	public void testFindAllByUserOrderBySentDate() {
		// When
		List<Counseling> counselings = counselingRepository.findAllByUserOrderBySentDate(testUser);

		// Then
		assertThat(counselings.size()).isEqualTo(2);
		assertThat(counselings.get(0)).isEqualTo(counseling1);
		assertThat(counselings.get(1)).isEqualTo(counseling2);
	}
}