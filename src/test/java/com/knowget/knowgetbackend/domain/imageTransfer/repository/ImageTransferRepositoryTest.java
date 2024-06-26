package com.knowget.knowgetbackend.domain.imageTransfer.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.knowget.knowgetbackend.global.entity.Image;
import com.knowget.knowgetbackend.global.entity.JobGuide;
import com.knowget.knowgetbackend.global.entity.User;

@DataJpaTest
class ImageTransferRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ImageTransferRepository imageTransferRepository;

	private User user;
	private JobGuide jobGuide1;
	private JobGuide jobGuide2;
	private Image image1;
	private Image image2;
	private Image image3;

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

		image1 = Image.builder()
			.imageUrl("http://example.com/image1")
			.jobGuide(jobGuide1)
			.build();
		image2 = Image.builder()
			.imageUrl("http://example.com/image2")
			.jobGuide(jobGuide1)
			.build();
		image3 = Image.builder()
			.imageUrl("http://example.com/image3")
			.jobGuide(jobGuide2)
			.build();

		entityManager.persist(image1);
		entityManager.persist(image2);
		entityManager.persist(image3);
		entityManager.flush();
	}

	@Test
	@DisplayName("특정 JobGuide에 대한 Image 조회 테스트 - findByJobGuide")
	public void testFindByJobGuide() {
		// When
		List<Image> imagesForJobGuide1 = imageTransferRepository.findByJobGuide(jobGuide1);
		List<Image> imagesForJobGuide2 = imageTransferRepository.findByJobGuide(jobGuide2);

		// Then
		assertThat(imagesForJobGuide1).hasSize(2);
		assertThat(imagesForJobGuide1).extracting(Image::getImageUrl)
			.containsExactlyInAnyOrder("http://example.com/image1", "http://example.com/image2");

		assertThat(imagesForJobGuide2).hasSize(1);
		assertThat(imagesForJobGuide2).extracting(Image::getImageUrl).containsExactly("http://example.com/image3");
	}

	@Test
	@DisplayName("JobGuide에 이미지가 없는 경우 테스트 - findByJobGuide")
	public void testFindByJobGuideNoImages() {
		// Given
		JobGuide jobGuide3 = JobGuide.builder()
			.user(user)
			.title("Job Guide 3")
			.content("Content for Job Guide 3")
			.build();
		entityManager.persist(jobGuide3);
		entityManager.flush();

		// When
		List<Image> imagesForJobGuide3 = imageTransferRepository.findByJobGuide(jobGuide3);

		// Then
		assertThat(imagesForJobGuide3).isEmpty();
	}
}