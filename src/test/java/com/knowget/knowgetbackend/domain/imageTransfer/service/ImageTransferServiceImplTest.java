package com.knowget.knowgetbackend.domain.imageTransfer.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.knowget.knowgetbackend.domain.imageTransfer.repository.ImageTransferRepository;
import com.knowget.knowgetbackend.domain.jobGuide.repository.JobGuideRepository;
import com.knowget.knowgetbackend.global.config.s3.AwsS3Util;
import com.knowget.knowgetbackend.global.entity.Image;
import com.knowget.knowgetbackend.global.entity.JobGuide;
import com.knowget.knowgetbackend.global.exception.ImageNotFoundException;

class ImageTransferServiceImplTest {
	@Mock
	private ImageTransferRepository imageTransferRepository;

	@Mock
	private JobGuideRepository jobGuideRepository;

	@Mock
	private AwsS3Util awsS3Util;

	@InjectMocks
	private ImageTransferServiceImpl imageTransferService;

	@Mock
	private MultipartFile multipartFile;

	private JobGuide jobGuide;
	private Image image;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);

		jobGuide = JobGuide.builder()
			.user(null) // Set to null or mock as needed
			.title("Job Guide 1")
			.content("Content for Job Guide 1")
			.build();

		image = Image.builder()
			.imageUrl("http://example.com/image1")
			.jobGuide(jobGuide)
			.build();
	}

	@Test
	@DisplayName("다중 이미지 업로드 테스트 - uploadFiles")
	public void testUploadFiles() {
		// Given
		when(jobGuideRepository.findById(1)).thenReturn(Optional.of(jobGuide));
		when(awsS3Util.uploadFile(any(MultipartFile.class))).thenReturn("http://example.com/image1");
		when(imageTransferRepository.save(any(Image.class))).thenReturn(image);
		when(imageTransferRepository.findByJobGuide(jobGuide)).thenReturn(Arrays.asList(image));

		// When
		List<String> result = imageTransferService.uploadFiles(Arrays.asList(multipartFile, multipartFile), 1);

		// Then
		assertThat(result).hasSize(1);
		assertThat(result).contains("http://example.com/image1");

		verify(jobGuideRepository, times(1)).findById(1);
		verify(awsS3Util, times(2)).uploadFile(any(MultipartFile.class));
		verify(imageTransferRepository, times(2)).save(any(Image.class));
		verify(imageTransferRepository, times(1)).findByJobGuide(jobGuide);
	}

	@Test
	@DisplayName("이미지 삭제 테스트 - deleteImage")
	public void testDeleteImage() {
		// Given
		when(imageTransferRepository.findById(1)).thenReturn(Optional.of(image));

		// When
		String result = imageTransferService.deleteImage(1);

		// Then
		assertThat(result).isEqualTo("이미지가 삭제되었습니다.");

		verify(imageTransferRepository, times(1)).findById(1);
		verify(imageTransferRepository, times(1)).delete(image);
	}

	@Test
	@DisplayName("이미지를 찾을 수 없는 경우 삭제 테스트 - deleteImage")
	public void testDeleteImageNotFound() {
		// Given
		when(imageTransferRepository.findById(1)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(ImageNotFoundException.class, () -> {
			imageTransferService.deleteImage(1);
		});

		verify(imageTransferRepository, times(1)).findById(1);
		verify(imageTransferRepository, times(0)).delete(any(Image.class));
	}

	@Test
	@DisplayName("파일 크기 초과 테스트 - uploadFiles")
	public void testUploadFilesMaxSizeExceeded() {
		// Given
		when(multipartFile.getSize()).thenReturn(60 * 1024 * 1024L); // 60MB

		// When & Then
		assertThrows(MaxUploadSizeExceededException.class, () -> {
			imageTransferService.uploadFiles(Arrays.asList(multipartFile), 1);
		});

		verify(jobGuideRepository, times(0)).findById(anyInt());
		verify(awsS3Util, times(0)).uploadFile(any(MultipartFile.class));
		verify(imageTransferRepository, times(0)).save(any(Image.class));
	}
}