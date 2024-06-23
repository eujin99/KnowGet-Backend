package com.knowget.knowgetbackend.domain.imageTransfer.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.knowget.knowgetbackend.global.entity.Admin;
import com.knowget.knowgetbackend.global.entity.Image;
import com.knowget.knowgetbackend.global.entity.JobGuide;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ImageRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ImageRepository imageRepository;

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
	@DisplayName("findByJobGuide: JobGuide에 연관된 이미지를 반환")
	void findByJobGuide_shouldReturnImages() {
		Image image1 = Image.builder()
			.imageUrl("url1")
			.jobGuide(jobGuide)
			.build();
		Image image2 = Image.builder()
			.imageUrl("url2")
			.jobGuide(jobGuide)
			.build();

		entityManager.persist(image1);
		entityManager.persist(image2);

		List<Image> images = imageRepository.findByJobGuide(jobGuide);

		assertNotNull(images);
		assertEquals(2, images.size());
		assertTrue(images.stream().anyMatch(img -> img.getImageUrl().equals("url1")));
		assertTrue(images.stream().anyMatch(img -> img.getImageUrl().equals("url2")));
	}

	@Test
	@DisplayName("findByJobGuide: 연관된 이미지가 없을 때 빈 목록 반환")
	void findByJobGuide_shouldReturnEmptyListWhenNoImages() {
		List<Image> images = imageRepository.findByJobGuide(jobGuide);

		assertNotNull(images);
		assertTrue(images.isEmpty());
	}
}