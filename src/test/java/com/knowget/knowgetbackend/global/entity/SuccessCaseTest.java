package com.knowget.knowgetbackend.global.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SuccessCaseTest {
	private SuccessCase successCase;
	private User user;

	@BeforeEach
	public void setUp() {
		user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.build();

		successCase = SuccessCase.builder()
			.user(user)
			.title("Success Story Title")
			.content("This is a success story content.")
			.build();
	}

	@Test
	@DisplayName("SuccessCase 엔티티 생성 테스트")
	public void testCreateSuccessCase() {
		// Given
		User user = this.user;
		String title = "Success Story Title";
		String content = "This is a success story content.";

		// When
		SuccessCase successCase = SuccessCase.builder()
			.user(user)
			.title(title)
			.content(content)
			.build();

		// Then
		assertThat(successCase.getUser()).isEqualTo(user);
		assertThat(successCase.getTitle()).isEqualTo(title);
		assertThat(successCase.getContent()).isEqualTo(content);
		assertThat(successCase.getIsApproved()).isEqualTo(0);
	}

	@Test
	@DisplayName("SuccessCase 엔티티 기본 생성자 테스트")
	public void testDefaultConstructor() {
		// When
		SuccessCase successCase = new SuccessCase();

		// Then
		assertThat(successCase).isNotNull();
	}

	@Test
	@DisplayName("SuccessCase 엔티티 제목 업데이트 테스트")
	public void testUpdateTitle() {
		// Given
		String newTitle = "Updated Success Story Title";

		// When
		successCase.updateTitle(newTitle);

		// Then
		assertThat(successCase.getTitle()).isEqualTo(newTitle);
	}

	@Test
	@DisplayName("SuccessCase 엔티티 내용 업데이트 테스트")
	public void testUpdateContent() {
		// Given
		String newContent = "This is an updated success story content.";

		// When
		successCase.updateContent(newContent);

		// Then
		assertThat(successCase.getContent()).isEqualTo(newContent);
	}

	@Test
	@DisplayName("SuccessCase 엔티티 승인 상태 업데이트 테스트")
	public void testUpdateIsApproved() {
		// Given
		Integer newIsApproved = 1;

		// When
		successCase.updateIsApproved(newIsApproved);

		// Then
		assertThat(successCase.getIsApproved()).isEqualTo(newIsApproved);
	}
}