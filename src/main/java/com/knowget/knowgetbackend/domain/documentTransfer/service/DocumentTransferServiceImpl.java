package com.knowget.knowgetbackend.domain.documentTransfer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.knowget.knowgetbackend.domain.documentTransfer.repository.DocumentTransferRepository;
import com.knowget.knowgetbackend.domain.jobGuide.repository.JobGuideRepository;
import com.knowget.knowgetbackend.global.config.s3.AwsS3Util2;
import com.knowget.knowgetbackend.global.entity.Document;
import com.knowget.knowgetbackend.global.entity.JobGuide;
import com.knowget.knowgetbackend.global.exception.DocumentNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentTransferServiceImpl implements DocumentTransferService {

	private final DocumentTransferRepository documentTransferRepository;
	private final JobGuideRepository jobGuideRepository;
	private final AwsS3Util2 awsS3Util;
	private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB

	// /**
	//  * 문서 업로드
	//  * @param file
	//  * @param jobGuideId
	//  * @return imageUrl
	//  * @auther 근엽
	//  */
	// @Override
	// @Transactional
	// public String uploadFile(MultipartFile file, Integer jobGuideId) {
	// 	checkFileSize(file);
	//
	// 	JobGuide jobGuide = jobGuideRepository.findById(jobGuideId)
	// 		.orElseThrow(() -> new IllegalArgumentException("해당하는 취업 가이드가 없습니다."));
	//
	// 	Document document = Document.builder()
	// 		.documentUrl(awsS3Util.uploadFile(file))
	// 		.jobGuide(jobGuide)
	// 		.build();
	//
	// 	documentTransferRepository.save(document);
	//
	// 	Document documentUrl = documentTransferRepository.findByJobGuide(jobGuide)
	// 		.orElseThrow(() -> new IllegalArgumentException("해당하는 문서가 없습니다."));
	//
	// 	return documentUrl.getDocumentUrl();
	// }

	/**
	 * 문서 다중 업로드
	 * @param files
	 * @param jobGuidId
	 * @return imageUrls
	 * @auther 근엽
	 */
	@Override
	@Transactional
	public List<String> uploadFiles(List<MultipartFile> files, Integer jobGuidId) {
		for (MultipartFile file : files) {
			checkFileSize(file);
		}

		List<String> imageUrlList = new ArrayList<>();

		JobGuide jobGuide = jobGuideRepository.findById(jobGuidId)
			.orElseThrow(() -> new IllegalArgumentException("해당하는 취업 가이드가 없습니다."));

		for (MultipartFile file : files) {
			Document document = Document.builder()
				.documentUrl(awsS3Util.uploadFile(file))
				.jobGuide(jobGuide)
				.build();

			documentTransferRepository.save(document);
		}

		for (Document document : documentTransferRepository.findByJobGuide(jobGuide)) {

			imageUrlList.add(document.getDocumentUrl());
			log.info("문서 URL : " + document.getDocumentUrl());
		}

		return imageUrlList;
	}

	/**
	 * 문서 삭제
	 * @param documentId
	 * @return message
	 * @auther 근엽
	 */
	@Override
	@Transactional
	public String deleteDocument(Integer documentId) {

		Document document = documentTransferRepository.findById(documentId)
			.orElseThrow(() -> new DocumentNotFoundException("해당하는 문서가 없습니다."));

		// awsS3Util.deleteFile(document.getDocumentUrl());
		documentTransferRepository.delete(document);

		return "문서가 삭제되었습니다.";
	}

	/**
	 * 파일 사이즈 체크
	 * @param file
	 * @auther 근엽
	 */
	private void checkFileSize(MultipartFile file) {
		if (file.getSize() > MAX_FILE_SIZE) {
			throw new MaxUploadSizeExceededException(file.getSize());
		}
	}

}
