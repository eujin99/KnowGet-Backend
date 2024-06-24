package com.knowget.knowgetbackend.domain.jobGuide.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.knowget.knowgetbackend.global.entity.JobGuide;
import com.knowget.knowgetbackend.global.entity.User;

@DataJpaTest
class JobGuideRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private JobGuideRepository jobGuideRepository;

	private User user;
	private JobGuide jobGuide;

	@BeforeEach
	public void setUp() {
		user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.role("ADMIN")
			.build();
		entityManager.persist(user);

		jobGuide = JobGuide.builder()
			.user(user)
			.title("Job Guide 1")
			.content("Content for Job Guide 1")
			.build();
		entityManager.persist(jobGuide);
		entityManager.flush();
	}

	@Test
	@DisplayName("JobGuide 저장 및 조회 테스트")
	public void testSaveAndFindById() {
		// When
		Optional<JobGuide> foundJobGuide = jobGuideRepository.findById(jobGuide.getGuideId());

		// Then
		assertThat(foundJobGuide).isPresent();
		assertThat(foundJobGuide.get().getTitle()).isEqualTo("Job Guide 1");
		assertThat(foundJobGuide.get().getContent()).isEqualTo("Content for Job Guide 1");
		assertThat(foundJobGuide.get().getUser()).isEqualTo(user);
	}

	@Test
	@DisplayName("JobGuide 수정 테스트")
	public void testUpdateJobGuide() {
		// Given
		jobGuide.updateTitle("Updated Job Guide");
		jobGuide.updateContent("Updated content for Job Guide");
		jobGuideRepository.save(jobGuide);

		// When
		Optional<JobGuide> updatedJobGuide = jobGuideRepository.findById(jobGuide.getGuideId());

		// Then
		assertThat(updatedJobGuide).isPresent();
		assertThat(updatedJobGuide.get().getTitle()).isEqualTo("Updated Job Guide");
		assertThat(updatedJobGuide.get().getContent()).isEqualTo("Updated content for Job Guide");
	}

	@Test
	@DisplayName("JobGuide 삭제 테스트")
	public void testDeleteJobGuide() {
		// When
		jobGuideRepository.delete(jobGuide);
		Optional<JobGuide> deletedJobGuide = jobGuideRepository.findById(jobGuide.getGuideId());

		// Then
		assertThat(deletedJobGuide).isNotPresent();
	}

	@Test
	@DisplayName("JobGuide를 찾을 수 없는 경우 테스트")
	public void testFindByIdNotFound() {
		// When
		Optional<JobGuide> foundJobGuide = jobGuideRepository.findById(-1);

		// Then
		assertThat(foundJobGuide).isNotPresent();
	}
}