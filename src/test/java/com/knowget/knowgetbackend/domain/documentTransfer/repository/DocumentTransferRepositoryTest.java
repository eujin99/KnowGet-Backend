package com.knowget.knowgetbackend.domain.documentTransfer.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.knowget.knowgetbackend.global.entity.Document;
import com.knowget.knowgetbackend.global.entity.JobGuide;
import com.knowget.knowgetbackend.global.entity.User;

@DataJpaTest
class DocumentTransferRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private DocumentTransferRepository documentTransferRepository;

	private User user;
	private JobGuide jobGuide1;
	private JobGuide jobGuide2;
	private Document document1;
	private Document document2;
	private Document document3;

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

		jobGuide1 = JobGuide.builder()
			.user(user)
			.title("Job Guide 1")
			.content("Content for Job Guide 1")
			.build();
		entityManager.persist(jobGuide1);

		jobGuide2 = JobGuide.builder()
			.user(user)
			.title("Job Guide 2")
			.content("Content for Job Guide 2")
			.build();
		entityManager.persist(jobGuide2);

		document1 = Document.builder()
			.documentUrl("http://example.com/doc1")
			.jobGuide(jobGuide1)
			.build();
		document2 = Document.builder()
			.documentUrl("http://example.com/doc2")
			.jobGuide(jobGuide1)
			.build();
		document3 = Document.builder()
			.documentUrl("http://example.com/doc3")
			.jobGuide(jobGuide2)
			.build();

		entityManager.persist(document1);
		entityManager.persist(document2);
		entityManager.persist(document3);
		entityManager.flush();
	}

	@Test
	@DisplayName("특정 JobGuide에 대한 Document 조회 테스트 - findByJobGuide")
	public void testFindByJobGuide() {
		// When
		List<Document> documentsForJobGuide1 = documentTransferRepository.findByJobGuide(jobGuide1);
		List<Document> documentsForJobGuide2 = documentTransferRepository.findByJobGuide(jobGuide2);

		// Then
		assertThat(documentsForJobGuide1).hasSize(2);
		assertThat(documentsForJobGuide1).extracting(Document::getDocumentUrl)
			.containsExactlyInAnyOrder("http://example.com/doc1", "http://example.com/doc2");

		assertThat(documentsForJobGuide2).hasSize(1);
		assertThat(documentsForJobGuide2).extracting(Document::getDocumentUrl)
			.containsExactly("http://example.com/doc3");
	}

	@Test
	@DisplayName("JobGuide에 문서가 없는 경우 테스트 - findByJobGuide")
	public void testFindByJobGuideNoDocuments() {
		// Given
		JobGuide jobGuide3 = JobGuide.builder()
			.user(user)
			.title("Job Guide 3")
			.content("Content for Job Guide 3")
			.build();
		entityManager.persist(jobGuide3);
		entityManager.flush();

		// When
		List<Document> documentsForJobGuide3 = documentTransferRepository.findByJobGuide(jobGuide3);

		// Then
		assertThat(documentsForJobGuide3).isEmpty();
	}
}