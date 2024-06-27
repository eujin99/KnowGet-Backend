package com.knowget.knowgetbackend.domain.reply.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.knowget.knowgetbackend.domain.successCase.repository.SuccessCaseRepository;
import com.knowget.knowgetbackend.global.entity.SuccessCase;
import com.knowget.knowgetbackend.global.entity.User;

import jakarta.transaction.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ReplyRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private SuccessCaseRepository successCaseRepository;

	private User user;
	private SuccessCase successCase1;
	private SuccessCase successCase2;

	@BeforeEach
	public void setUp() {
		user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.role("USER")
			.build();
		entityManager.persist(user);

		successCase1 = SuccessCase.builder()
			.user(user)
			.title("Success Case 1")
			.content("Content for Success Case 1")
			.build();

		successCase2 = SuccessCase.builder()
			.user(user)
			.title("Success Case 2")
			.content("Content for Success Case 2")
			.build();

		entityManager.persist(successCase1);
		entityManager.persist(successCase2);
		entityManager.flush();
	}

	@Test
	@DisplayName("제목에 키워드를 포함한 성공 사례 검색 테스트")
	public void testFindByTitleContaining() {
		// When
		List<SuccessCase> successCases = successCaseRepository.findByTitleContaining("Success");

		// Then
		assertThat(successCases).hasSize(2);
		assertThat(successCases).extracting(SuccessCase::getTitle)
			.containsExactlyInAnyOrder("Success Case 1", "Success Case 2");
	}

	@Test
	@DisplayName("성공 사례 승인 상태 업데이트 테스트")
	@Transactional
	@Rollback
	public void testUpdateApprovalStatus() {
		// Given
		Integer caseId = successCase1.getCaseId();

		// When
		successCaseRepository.updateApprovalStatus(caseId, (short)1);
		entityManager.flush();
		entityManager.clear();

		SuccessCase updatedSuccessCase = entityManager.find(SuccessCase.class, caseId);

		// Then
		assertThat(updatedSuccessCase.getIsApproved()).isEqualTo(1);
	}

	@Test
	@DisplayName("특정 사용자의 성공 사례 목록 조회 테스트")
	public void testFindAllByUserOrderByCreatedDateDesc() {
		// When
		List<SuccessCase> successCases = successCaseRepository.findAllByUserOrderByCreatedDateDesc(user);

		// Then
		assertThat(successCases).hasSize(2);
		assertThat(successCases).extracting(SuccessCase::getTitle)
			.containsExactly("Success Case 2", "Success Case 1");
	}

	@Test
	@DisplayName("성공 사례 저장 및 조회 테스트")
	public void testSaveAndFindById() {
		// Given
		SuccessCase successCase = SuccessCase.builder()
			.user(user)
			.title("New Success Case")
			.content("Content for new Success Case")
			.build();
		successCase = successCaseRepository.save(successCase);

		// When
		SuccessCase foundSuccessCase = successCaseRepository.findById(successCase.getCaseId()).orElse(null);

		// Then
		assertThat(foundSuccessCase).isNotNull();
		assertThat(foundSuccessCase.getTitle()).isEqualTo("New Success Case");
	}

	@Test
	@DisplayName("성공 사례 삭제 테스트")
	public void testDeleteSuccessCase() {
		// Given
		SuccessCase successCase = SuccessCase.builder()
			.user(user)
			.title("Success Case to delete")
			.content("Content for Success Case to delete")
			.build();
		successCase = successCaseRepository.save(successCase);

		// When
		successCaseRepository.delete(successCase);
		SuccessCase foundSuccessCase = successCaseRepository.findById(successCase.getCaseId()).orElse(null);

		// Then
		assertThat(foundSuccessCase).isNull();
	}
}