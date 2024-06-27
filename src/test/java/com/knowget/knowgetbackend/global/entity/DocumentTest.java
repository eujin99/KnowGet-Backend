package com.knowget.knowgetbackend.global.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DocumentTest {
	private JobGuide jobGuide;

	@BeforeEach
	@DisplayName("설정 - Admin 및 JobGuide 객체 생성")
	public void setUp() {
		User admin = User.builder()
			.username("admin")
			.password("password")
			.prefLocation("NULL")
			.prefJob("NULL")
			.role("ADMIN")
			.build();

		jobGuide = JobGuide.builder()
			.user(admin)
			.title("Sample Job Guide")
			.content("This is a sample job guide content")
			.build();
	}

	@Test
	@DisplayName("Document 객체 생성 테스트")
	public void testDocumentBuilder() {
		// Given
		String documentUrl = "http://example.com/document.pdf";

		// When
		Document document = Document.builder()
			.documentUrl(documentUrl)
			.jobGuide(jobGuide)
			.build();

		// Then
		assertThat(document.getDocumentUrl()).isEqualTo(documentUrl);
		assertThat(document.getJobGuide()).isEqualTo(jobGuide);
	}

	@Test
	@DisplayName("Document 객체 기본 생성자 테스트")
	public void testNoArgsConstructor() {
		// When
		Document document = new Document();

		// Then
		assertThat(document.getDocumentId()).isNull();
		assertThat(document.getDocumentUrl()).isNull();
		assertThat(document.getJobGuide()).isNull();
	}
}