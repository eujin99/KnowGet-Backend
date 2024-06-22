package com.knowget.knowgetbackend.global.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentTest {
	private Comment comment;
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
			.title("Success Story")
			.content("This is a success story content.")
			.user(user)
			.build();

		comment = Comment.builder()
			.successCase(successCase)
			.user(user)
			.content("This is a comment")
			.build();
	}

	@Test
	@DisplayName("Comment 엔티티 생성 테스트")
	public void testCreateComment() {
		// Given
		SuccessCase successCase = this.successCase;
		User user = this.user;
		String content = "This is a comment";

		// When
		Comment comment = Comment.builder()
			.successCase(successCase)
			.user(user)
			.content(content)
			.build();

		// Then
		assertThat(comment.getSuccessCase()).isEqualTo(successCase);
		assertThat(comment.getUser()).isEqualTo(user);
		assertThat(comment.getContent()).isEqualTo(content);
	}

	@Test
	@DisplayName("Comment 엔티티 기본 생성자 테스트")
	public void testDefaultConstructor() {
		// When
		Comment comment = new Comment();

		// Then
		assertThat(comment).isNotNull();
	}

	@Test
	@DisplayName("Comment 엔티티 콘텐츠 업데이트 테스트")
	public void testUpdateContent() {
		// Given
		String newContent = "This is an updated comment";

		// When
		comment.updateContent(newContent);

		// Then
		assertThat(comment.getContent()).isEqualTo(newContent);
	}
}