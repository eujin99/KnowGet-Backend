package com.knowget.knowgetbackend.global.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnswerTest {
	private Answer answer;
	private Counseling counseling;
	private User admin;

	@BeforeEach
	public void setUp() {
		counseling = Counseling.builder()
			.user(User.builder()
				.username("testuser")
				.password("password")
				.prefLocation("Seoul")
				.prefJob("Engineer")
				.build())
			.category("Test Category")
			.content("This is a test content.")
			.build();

		admin = User.builder()
			.username("admin")
			.password("password")
			.prefLocation("NULL")
			.prefJob("NULL")
			.role("ADMIN")
			.build();

		answer = Answer.builder()
			.counseling(counseling)
			.content("This is an answer")
			.build();
	}

	@Test
	@DisplayName("Answer 엔티티 생성 테스트")
	public void testCreateAnswer() {
		// Given
		Counseling counseling = this.counseling;
		User admin = this.admin;
		String content = "This is an answer";

		// When
		Answer answer = Answer.builder()
			.counseling(counseling)
			.content(content)
			.build();

		// Then
		assertThat(answer.getCounseling()).isEqualTo(counseling);
		assertThat(answer.getContent()).isEqualTo(content);
	}

	@Test
	@DisplayName("Answer 엔티티 기본 생성자 테스트")
	public void testDefaultConstructor() {
		// When
		Answer answer = new Answer();

		// Then
		assertThat(answer).isNotNull();
	}

	@Test
	@DisplayName("Answer 엔티티 콘텐츠 업데이트 테스트")
	public void testUpdateContent() {
		// Given
		String newContent = "This is an updated answer";

		// When
		answer.updateContent(newContent);

		// Then
		assertThat(answer.getContent()).isEqualTo(newContent);
	}
}