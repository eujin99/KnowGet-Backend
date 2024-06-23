package com.knowget.knowgetbackend.domain.imageTransfer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.knowget.knowgetbackend.domain.imageTransfer.repository.ImageRepository;
import com.knowget.knowgetbackend.domain.jobGuide.repository.JobGuideRepository;
import com.knowget.knowgetbackend.global.config.s3.AwsS3Util;
import com.knowget.knowgetbackend.global.entity.Admin;
import com.knowget.knowgetbackend.global.entity.Image;
import com.knowget.knowgetbackend.global.entity.JobGuide;
import com.knowget.knowgetbackend.global.exception.ImageNotFoundException;

@ExtendWith(MockitoExtension.class)
class ImageTransferServiceImplTest {
	@Mock
	private ImageRepository imageRepository;

	@Mock
	private JobGuideRepository jobGuideRepository;

	@Mock
	private AwsS3Util awsS3Util;

	@InjectMocks
	private ImageTransferServiceImpl imageTransferService;

	@BeforeEach
	@DisplayName("Mock 설정")
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("uploadFiles 성공 케이스")
	void uploadFiles_success() {
		MultipartFile file1 = mock(MultipartFile.class);
		MultipartFile file2 = mock(MultipartFile.class);

		List<MultipartFile> files = Arrays.asList(file1, file2);

		Admin admin = Admin.builder()
			.username("admin")
			.password("password")
			.build();

		JobGuide jobGuide = JobGuide.builder()
			.admin(admin)
			.title("Test Title")
			.content("Test Content")
			.build();

		when(jobGuideRepository.findById(anyInt())).thenReturn(Optional.of(jobGuide));
		when(awsS3Util.uploadFile(any(MultipartFile.class))).thenReturn("mockUrl1", "mockUrl2");
		when(imageRepository.save(any(Image.class)))
			.thenReturn(Image.builder().imageUrl("mockUrl1").jobGuide(jobGuide).build(),
				Image.builder().imageUrl("mockUrl2").jobGuide(jobGuide).build());
		when(imageRepository.findByJobGuide(any(JobGuide.class)))
			.thenReturn(Arrays.asList(Image.builder().imageUrl("mockUrl1").jobGuide(jobGuide).build(),
				Image.builder().imageUrl("mockUrl2").jobGuide(jobGuide).build()));

		List<String> result = imageTransferService.uploadFiles(files, 1);

		assertEquals(2, result.size());
		assertTrue(result.contains("mockUrl1"));
		assertTrue(result.contains("mockUrl2"));

		verify(jobGuideRepository, times(1)).findById(1);
		verify(awsS3Util, times(2)).uploadFile(any(MultipartFile.class));
		verify(imageRepository, times(2)).save(any(Image.class));
		verify(imageRepository, times(1)).findByJobGuide(any(JobGuide.class));
	}

	@Test
	@DisplayName("uploadFiles 실패 케이스: JobGuide 찾을 수 없음")
	void uploadFiles_jobGuideNotFound() {
		MultipartFile file1 = mock(MultipartFile.class);
		MultipartFile file2 = mock(MultipartFile.class);

		List<MultipartFile> files = Arrays.asList(file1, file2);

		when(jobGuideRepository.findById(anyInt())).thenReturn(Optional.empty());

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			imageTransferService.uploadFiles(files, 1);
		});

		assertEquals("해당하는 취업 가이드가 없습니다.", exception.getMessage());

		verify(jobGuideRepository, times(1)).findById(1);
		verify(awsS3Util, times(0)).uploadFile(any(MultipartFile.class));
		verify(imageRepository, times(0)).save(any(Image.class));
	}

	@Test
	@DisplayName("uploadFiles 실패 케이스: 파일 크기 초과")
	void uploadFiles_checkFileSizeExceeded() {
		MultipartFile file1 = mock(MultipartFile.class);
		MultipartFile file2 = mock(MultipartFile.class);

		when(file1.getSize()).thenReturn(60 * 1024 * 1024L); // 60MB
		when(file2.getSize()).thenReturn(60 * 1024 * 1024L); // 60MB

		List<MultipartFile> files = Arrays.asList(file1, file2);

		MaxUploadSizeExceededException exception = assertThrows(MaxUploadSizeExceededException.class, () -> {
			imageTransferService.uploadFiles(files, 1);
		});

		assertEquals(60 * 1024 * 1024L, exception.getMaxUploadSize());

		verify(jobGuideRepository, times(0)).findById(anyInt());
		verify(awsS3Util, times(0)).uploadFile(any(MultipartFile.class));
		verify(imageRepository, times(0)).save(any(Image.class));
	}

	@Test
	@DisplayName("deleteImage 성공 케이스")
	void deleteImage_success() {
		Image image = Image.builder()
			.imageUrl("mockUrl")
			.jobGuide(JobGuide.builder().build())
			.build();

		when(imageRepository.findById(anyInt())).thenReturn(Optional.of(image));

		String result = imageTransferService.deleteImage(1);

		assertEquals("이미지가 삭제되었습니다.", result);

		verify(imageRepository, times(1)).findById(1);
		verify(imageRepository, times(1)).delete(image);
	}

	@Test
	@DisplayName("deleteImage 실패 케이스: Image 찾을 수 없음")
	void deleteImage_imageNotFound() {
		when(imageRepository.findById(anyInt())).thenReturn(Optional.empty());

		ImageNotFoundException exception = assertThrows(ImageNotFoundException.class, () -> {
			imageTransferService.deleteImage(1);
		});

		assertEquals("해당하는 이미지가 없습니다.", exception.getMessage());

		verify(imageRepository, times(1)).findById(1);
		verify(imageRepository, times(0)).delete(any(Image.class));
	}
}