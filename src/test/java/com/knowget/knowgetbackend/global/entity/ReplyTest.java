package com.knowget.knowgetbackend.global.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReplyTest {
	private Reply reply;
	private Comment comment;
	private User user;

	@BeforeEach
	public void setUp() {
		user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.build();

		comment = Comment.builder()
			.successCase(SuccessCase.builder()
				.user(user)
				.title("Success Story")
				.content("This is a success story content.")
				.build())
			.user(user)
			.content("This is a comment")
			.build();

		reply = Reply.builder()
			.comment(comment)
			.user(user)
			.content("This is a reply")
			.build();
	}

	@Test
	@DisplayName("Reply 엔티티 생성 테스트")
	public void testCreateReply() {
		// Given
		Comment comment = this.comment;
		User user = this.user;
		String content = "This is a reply";

		// When
		Reply reply = Reply.builder()
			.comment(comment)
			.user(user)
			.content(content)
			.build();

		// Then
		assertThat(reply.getComment()).isEqualTo(comment);
		assertThat(reply.getUser()).isEqualTo(user);
		assertThat(reply.getContent()).isEqualTo(content);
	}

	@Test
	@DisplayName("Reply 엔티티 기본 생성자 테스트")
	public void testDefaultConstructor() {
		// When
		Reply reply = new Reply();

		// Then
		assertThat(reply).isNotNull();
	}

	@Test
	@DisplayName("Reply 엔티티 콘텐츠 업데이트 테스트")
	public void testUpdateContent() {
		// Given
		String newContent = "This is an updated reply";

		// When
		reply.updateContent(newContent);

		// Then
		assertThat(reply.getContent()).isEqualTo(newContent);
	}
}