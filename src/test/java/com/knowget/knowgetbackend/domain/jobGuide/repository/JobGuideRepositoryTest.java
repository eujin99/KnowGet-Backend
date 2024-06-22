package com.knowget.knowgetbackend.domain.jobGuide.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.knowget.knowgetbackend.global.entity.Admin;
import com.knowget.knowgetbackend.global.entity.JobGuide;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class JobGuideRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private JobGuideRepository jobGuideRepository;

	private Admin admin;
	private JobGuide jobGuide1;
	private JobGuide jobGuide2;

	@BeforeEach
	public void setUp() {
		admin = Admin.builder()
			.username("admin")
			.password("password")
			.build();
		entityManager.persist(admin);

		jobGuide1 = JobGuide.builder()
			.admin(admin)
			.title("Job Guide 1")
			.content("Content for Job Guide 1")
			.build();
		jobGuide2 = JobGuide.builder()
			.admin(admin)
			.title("Job Guide 2")
			.content("Content for Job Guide 2")
			.build();

		entityManager.persist(jobGuide1);
		entityManager.persist(jobGuide2);
		entityManager.flush();
	}

	@Test
	@DisplayName("JobGuide 저장 및 조회 테스트")
	public void testSaveAndFindById() {
		// When
		Optional<JobGuide> foundJobGuide = jobGuideRepository.findById(jobGuide1.getGuideId());

		// Then
		assertThat(foundJobGuide).isPresent();
		assertThat(foundJobGuide.get().getTitle()).isEqualTo("Job Guide 1");
	}

	@Test
	@DisplayName("모든 JobGuide 조회 테스트")
	public void testFindAll() {
		// When
		List<JobGuide> jobGuides = jobGuideRepository.findAll();

		// Then
		assertThat(jobGuides).hasSize(2);
		assertThat(jobGuides).contains(jobGuide1, jobGuide2);
	}

	@Test
	@DisplayName("JobGuide 삭제 테스트")
	public void testDelete() {
		// When
		jobGuideRepository.delete(jobGuide1);
		List<JobGuide> jobGuides = jobGuideRepository.findAll();

		// Then
		assertThat(jobGuides).hasSize(1);
		assertThat(jobGuides).doesNotContain(jobGuide1);
	}

	@Test
	@DisplayName("JobGuide 업데이트 테스트")
	public void testUpdate() {
		// Given
		jobGuide1.updateTitle("Updated Job Guide 1");
		jobGuide1.updateContent("Updated Content for Job Guide 1");

		// When
		jobGuideRepository.save(jobGuide1);
		Optional<JobGuide> updatedJobGuide = jobGuideRepository.findById(jobGuide1.getGuideId());

		// Then
		assertThat(updatedJobGuide).isPresent();
		assertThat(updatedJobGuide.get().getTitle()).isEqualTo("Updated Job Guide 1");
		assertThat(updatedJobGuide.get().getContent()).isEqualTo("Updated Content for Job Guide 1");
	}
}