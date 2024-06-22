package com.knowget.knowgetbackend.domain.answer.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.knowget.knowgetbackend.global.entity.Admin;
import com.knowget.knowgetbackend.global.entity.Answer;
import com.knowget.knowgetbackend.global.entity.Counseling;
import com.knowget.knowgetbackend.global.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class AnswerRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private AnswerRepository answerRepository;

	private Admin admin;
	private Counseling counseling;
	private Answer answer;
	private User testUser;

	@BeforeEach
	public void setUp() {
		admin = Admin.builder()
			.username("adminuser")
			.password("password")
			.build();
		entityManager.persist(admin);

		testUser = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.build();
		entityManager.persist(testUser);

		counseling = Counseling.builder()
			.user(testUser)
			.category("Career")
			.content("This is a counseling content.")
			.build();
		entityManager.persist(counseling);

		answer = Answer.builder()
			.admin(admin)
			.counseling(counseling)
			.content("This is an answer.")
			.build();
		entityManager.persist(answer);
		entityManager.flush();
	}

	@Test
	@DisplayName("Answer 엔티티 저장 및 조회 테스트")
	public void testSaveAndFindById() {
		// When
		Optional<Answer> foundAnswer = answerRepository.findById(answer.getAnswerId());

		// Then
		assertThat(foundAnswer).isPresent();
		assertThat(foundAnswer.get().getContent()).isEqualTo(answer.getContent());
		assertThat(foundAnswer.get().getAdmin()).isEqualTo(admin);
		assertThat(foundAnswer.get().getCounseling()).isEqualTo(counseling);
	}

	@Test
	@DisplayName("존재하지 않는 Answer 조회 테스트")
	public void testFindByIdNotFound() {
		// When
		Optional<Answer> foundAnswer = answerRepository.findById(-1);

		// Then
		assertThat(foundAnswer).isNotPresent();
	}
}