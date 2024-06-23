package com.knowget.knowgetbackend.domain.documentTransfer.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.knowget.knowgetbackend.global.entity.Admin;
import com.knowget.knowgetbackend.global.entity.Document;
import com.knowget.knowgetbackend.global.entity.JobGuide;

@DataJpaTest
class DocumentTransferRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private DocumentTransferRepository documentTransferRepository;

	private Admin admin;
	private JobGuide jobGuide;

	@BeforeEach
	@DisplayName("기본 설정")
	void setUp() {
		admin = Admin.builder()
			.username("admin")
			.password("password")
			.build();
		entityManager.persist(admin);

		jobGuide = JobGuide.builder()
			.admin(admin)
			.title("Test Job Guide")
			.content("This is a test job guide.")
			.build();
		entityManager.persist(jobGuide);
	}

	@Test
	@DisplayName("findByJobGuide: JobGuide에 연관된 문서를 반환")
	void findByJobGuide_shouldReturnDocuments() {
		Document document1 = Document.builder()
			.documentUrl("url1")
			.jobGuide(jobGuide)
			.build();
		Document document2 = Document.builder()
			.documentUrl("url2")
			.jobGuide(jobGuide)
			.build();

		entityManager.persist(document1);
		entityManager.persist(document2);

		List<Document> documents = documentTransferRepository.findByJobGuide(jobGuide);

		assertNotNull(documents);
		assertEquals(2, documents.size());
		assertTrue(documents.stream().anyMatch(doc -> doc.getDocumentUrl().equals("url1")));
		assertTrue(documents.stream().anyMatch(doc -> doc.getDocumentUrl().equals("url2")));
	}

	@Test
	@DisplayName("findByJobGuide: 연관된 문서가 없을 때 빈 목록 반환")
	void findByJobGuide_shouldReturnEmptyListWhenNoDocuments() {
		List<Document> documents = documentTransferRepository.findByJobGuide(jobGuide);

		assertNotNull(documents);
		assertTrue(documents.isEmpty());
	}
}