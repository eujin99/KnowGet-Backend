package com.knowget.knowgetbackend.global.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JobGuideTest {
	private JobGuide jobGuide;
	private User admin;

	@BeforeEach
	public void setUp() {
		admin = User.builder()
			.username("adminuser")
			.password("password")
			.prefJob("NULL")
			.prefLocation("NULL")
			.role("ADMIN")
			.build();

		jobGuide = JobGuide.builder()
			.user(admin)
			.title("Job Guide Title")
			.content("This is job guide content.")
			.build();
	}

	@Test
	@DisplayName("JobGuide 엔티티 생성 테스트")
	public void testCreateJobGuide() {
		// Given
		User admin = this.admin;
		String title = "Job Guide Title";
		String content = "This is job guide content.";

		// When
		JobGuide jobGuide = JobGuide.builder()
			.user(admin)
			.title(title)
			.content(content)
			.build();

		// Then
		assertThat(jobGuide.getUser()).isEqualTo(admin);
		assertThat(jobGuide.getTitle()).isEqualTo(title);
		assertThat(jobGuide.getContent()).isEqualTo(content);
	}

	@Test
	@DisplayName("JobGuide 엔티티 기본 생성자 테스트")
	public void testDefaultConstructor() {
		// When
		JobGuide jobGuide = new JobGuide();

		// Then
		assertThat(jobGuide).isNotNull();
	}

	@Test
	@DisplayName("JobGuide 엔티티 제목 업데이트 테스트")
	public void testUpdateTitle() {
		// Given
		String newTitle = "Updated Job Guide Title";

		// When
		jobGuide.updateTitle(newTitle);

		// Then
		assertThat(jobGuide.getTitle()).isEqualTo(newTitle);
	}

	@Test
	@DisplayName("JobGuide 엔티티 내용 업데이트 테스트")
	public void testUpdateContent() {
		// Given
		String newContent = "This is updated job guide content.";

		// When
		jobGuide.updateContent(newContent);

		// Then
		assertThat(jobGuide.getContent()).isEqualTo(newContent);
	}
}