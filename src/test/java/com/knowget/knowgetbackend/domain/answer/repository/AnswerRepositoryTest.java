package com.knowget.knowgetbackend.domain.answer.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.knowget.knowgetbackend.domain.counseling.repository.CounselingRepository;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.Answer;
import com.knowget.knowgetbackend.global.entity.Counseling;
import com.knowget.knowgetbackend.global.entity.User;

@DataJpaTest
class AnswerRepositoryTest {
	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private CounselingRepository counselingRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("Answer를 저장하고 조회할 수 있어야 한다")
	void testSaveAndFindById() {
		// Given
		User user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("location")
			.prefJob("job")
			.role("USER")
			.build();
		User savedUser = userRepository.save(user);

		Counseling counseling = Counseling.builder()
			.user(savedUser)
			.category("Test Category")
			.content("Test Counseling")
			.build();
		Counseling savedCounseling = counselingRepository.save(counseling);

		Answer answer = Answer.builder()
			.counseling(savedCounseling)
			.content("Test Answer")
			.build();
		Answer savedAnswer = answerRepository.save(answer);

		// When
		Optional<Answer> retrievedAnswer = answerRepository.findById(savedAnswer.getAnswerId());

		// Then
		assertThat(retrievedAnswer).isPresent();
		assertThat(retrievedAnswer.get().getContent()).isEqualTo("Test Answer");
		assertThat(retrievedAnswer.get().getCounseling().getContent()).isEqualTo("Test Counseling");
	}

	@Test
	@DisplayName("Answer 목록을 조회할 수 있어야 한다")
	void testFindAll() {
		// Given
		User user1 = User.builder()
			.username("user1")
			.password("password")
			.prefLocation("location1")
			.prefJob("job1")
			.role("USER")
			.build();
		User savedUser1 = userRepository.save(user1);

		User user2 = User.builder()
			.username("user2")
			.password("password")
			.prefLocation("location2")
			.prefJob("job2")
			.role("USER")
			.build();
		User savedUser2 = userRepository.save(user2);

		Counseling counseling1 = Counseling.builder()
			.user(savedUser1)
			.category("Category 1")
			.content("Counseling 1")
			.build();
		Counseling counseling2 = Counseling.builder()
			.user(savedUser2)
			.category("Category 2")
			.content("Counseling 2")
			.build();
		counselingRepository.saveAll(List.of(counseling1, counseling2));

		Answer answer1 = Answer.builder().counseling(counseling1).content("Answer 1").build();
		Answer answer2 = Answer.builder().counseling(counseling2).content("Answer 2").build();
		answerRepository.saveAll(List.of(answer1, answer2));

		// When
		List<Answer> answers = answerRepository.findAll();

		// Then
		assertThat(answers).hasSize(2);
		assertThat(answers).extracting(Answer::getContent).containsExactlyInAnyOrder("Answer 1", "Answer 2");
	}

	@Test
	@DisplayName("Answer를 삭제할 수 있어야 한다")
	void testDelete() {
		// Given
		User user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("location")
			.prefJob("job")
			.role("USER")
			.build();
		User savedUser = userRepository.save(user);

		Counseling counseling = Counseling.builder()
			.user(savedUser)
			.category("Category to be deleted")
			.content("Counseling to be deleted")
			.build();
		Counseling savedCounseling = counselingRepository.save(counseling);

		Answer answer = Answer.builder()
			.counseling(savedCounseling)
			.content("Answer to be deleted")
			.build();
		Answer savedAnswer = answerRepository.save(answer);

		// When
		answerRepository.deleteById(savedAnswer.getAnswerId());
		Optional<Answer> deletedAnswer = answerRepository.findById(savedAnswer.getAnswerId());

		// Then
		assertThat(deletedAnswer).isNotPresent();
	}
}