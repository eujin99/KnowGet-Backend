package com.knowget.knowgetbackend.global.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CounselingTest {

	private Counseling counseling;
	private User user;

	@BeforeEach
	public void setUp() {
		user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.build();

		counseling = Counseling.builder()
			.user(user)
			.category("Career")
			.content("This is a counseling content.")
			.build();
	}

	@Test
	@DisplayName("Counseling 엔티티 생성 테스트")
	public void testCreateCounseling() {
		// Given
		User user = this.user;
		String category = "Career";
		String content = "This is a counseling content.";

		// When
		Counseling counseling = Counseling.builder()
			.user(user)
			.category(category)
			.content(content)
			.build();

		// Then
		assertThat(counseling.getUser()).isEqualTo(user);
		assertThat(counseling.getCategory()).isEqualTo(category);
		assertThat(counseling.getContent()).isEqualTo(content);
		assertThat(counseling.getIsAnswered()).isFalse();
	}

	@Test
	@DisplayName("Counseling 엔티티 기본 생성자 테스트")
	public void testDefaultConstructor() {
		// When
		Counseling counseling = new Counseling();

		// Then
		assertThat(counseling).isNotNull();
	}

	@Test
	@DisplayName("Counseling 엔티티 답변 상태 업데이트 테스트")
	public void testUpdateIsAnswered() {
		// Given
		Boolean newIsAnswered = true;

		// When
		counseling.updateIsAnswered(newIsAnswered);

		// Then
		assertThat(counseling.getIsAnswered()).isEqualTo(newIsAnswered);
	}
}