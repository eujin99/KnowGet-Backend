package com.knowget.knowgetbackend.global.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ImageTest {
	private User admin;
	private JobGuide jobGuide;

	@BeforeEach
	@DisplayName("설정 - Admin 및 JobGuide 객체 생성")
	public void setUp() {
		admin = User.builder()
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
	@DisplayName("Image 객체 생성 테스트")
	public void testImageBuilder() {
		// Given
		String imageUrl = "http://example.com/image.jpg";

		// When
		Image image = Image.builder()
			.imageUrl(imageUrl)
			.jobGuide(jobGuide)
			.build();

		// Then
		assertThat(image.getImageUrl()).isEqualTo(imageUrl);
		assertThat(image.getJobGuide()).isEqualTo(jobGuide);
	}

	@Test
	@DisplayName("Image 객체 기본 생성자 테스트")
	public void testNoArgsConstructor() {
		// When
		Image image = new Image();

		// Then
		assertThat(image.getImageId()).isNull();
		assertThat(image.getImageUrl()).isNull();
		assertThat(image.getJobGuide()).isNull();
	}
}