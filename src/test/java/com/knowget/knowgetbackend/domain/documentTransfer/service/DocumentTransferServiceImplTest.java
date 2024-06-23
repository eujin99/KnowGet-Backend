package com.knowget.knowgetbackend.domain.documentTransfer.service;

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

import com.knowget.knowgetbackend.domain.documentTransfer.repository.DocumentTransferRepository;
import com.knowget.knowgetbackend.domain.jobGuide.repository.JobGuideRepository;
import com.knowget.knowgetbackend.global.config.s3.AwsS3Util2;
import com.knowget.knowgetbackend.global.entity.Document;
import com.knowget.knowgetbackend.global.entity.JobGuide;
import com.knowget.knowgetbackend.global.entity.User;

class DocumentTransferServiceImplTest {
	@Mock
	private DocumentTransferRepository documentTransferRepository;

	@Mock
	private JobGuideRepository jobGuideRepository;

	@Mock
	private AwsS3Util2 awsS3Util;

	@InjectMocks
	private DocumentTransferServiceImpl documentTransferService;

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

		User admin = User.builder()
			.username("admin")
			.password("password")
			.prefLocation("NULL")
			.prefJob("NULL")
			.role("ADMIN")
			.build();

		JobGuide jobGuide = JobGuide.builder()
			.user(admin)
			.title("Test Title")
			.content("Test Content")
			.build();

		when(jobGuideRepository.findById(anyInt())).thenReturn(Optional.of(jobGuide));
		when(awsS3Util.uploadFile(any(MultipartFile.class))).thenReturn("mockUrl1", "mockUrl2");
		when(documentTransferRepository.save(any(Document.class)))
			.thenReturn(Document.builder().documentUrl("mockUrl1").jobGuide(jobGuide).build());
		when(documentTransferRepository.findByJobGuide(any(JobGuide.class)))
			.thenReturn(Arrays.asList(Document.builder().documentUrl("mockUrl1").jobGuide(jobGuide).build(),
				Document.builder().documentUrl("mockUrl2").jobGuide(jobGuide).build()));

		List<String> result = documentTransferService.uploadFiles(files, 1);

		assertEquals(2, result.size());
		assertTrue(result.contains("mockUrl1"));
		assertTrue(result.contains("mockUrl2"));

		verify(jobGuideRepository, times(1)).findById(1);
		verify(awsS3Util, times(2)).uploadFile(any(MultipartFile.class));
		verify(documentTransferRepository, times(2)).save(any(Document.class));
		verify(documentTransferRepository, times(1)).findByJobGuide(any(JobGuide.class));
	}

	@Test
	@DisplayName("uploadFiles 실패 케이스: JobGuide 찾을 수 없음")
	void uploadFiles_jobGuideNotFound() {
		MultipartFile file1 = mock(MultipartFile.class);
		MultipartFile file2 = mock(MultipartFile.class);

		List<MultipartFile> files = Arrays.asList(file1, file2);

		when(jobGuideRepository.findById(anyInt())).thenReturn(Optional.empty());

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			documentTransferService.uploadFiles(files, 1);
		});

		assertEquals("해당하는 취업 가이드가 없습니다.", exception.getMessage());

		verify(jobGuideRepository, times(1)).findById(1);
		verify(awsS3Util, times(0)).uploadFile(any(MultipartFile.class));
		verify(documentTransferRepository, times(0)).save(any(Document.class));
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
			documentTransferService.uploadFiles(files, 1);
		});

		assertEquals(60 * 1024 * 1024L, exception.getMaxUploadSize());

		verify(jobGuideRepository, times(0)).findById(anyInt());
		verify(awsS3Util, times(0)).uploadFile(any(MultipartFile.class));
		verify(documentTransferRepository, times(0)).save(any(Document.class));
	}
}