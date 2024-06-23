package com.knowget.knowgetbackend.domain.imageTransfer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.knowget.knowgetbackend.domain.imageTransfer.repository.ImageRepository;
import com.knowget.knowgetbackend.domain.jobGuide.repository.JobGuideRepository;
import com.knowget.knowgetbackend.global.config.s3.AwsS3Util;
import com.knowget.knowgetbackend.global.entity.Image;
import com.knowget.knowgetbackend.global.entity.JobGuide;
import com.knowget.knowgetbackend.global.exception.ImageNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageTransferServiceImpl implements ImageTransferService {
	private final ImageRepository imageRepository;
	private final JobGuideRepository jobGuideRepository;

	private final AwsS3Util awsS3Util;
	private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB

	// /**
	//  * 이미지 업로드
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
	// 	Image image = Image.builder()
	// 		.imageUrl(awsS3Util.uploadFile(file))
	// 		.jobGuide(jobGuide)
	// 		.build();
	//
	// 	imageRepository.save(image);
	//
	// 	Image imageUrl = imageRepository.findByJobGuide(jobGuide)
	// 		.orElseThrow(() -> new IllegalArgumentException("해당하는 이미지가 없습니다."));
	//
	// 	return imageUrl.getImageUrl();
	// }

	/**
	 * 이미지 다중 업로드
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
			Image image = Image.builder()
				.imageUrl(awsS3Util.uploadFile(file))
				.jobGuide(jobGuide)
				.build();

			imageRepository.save(image);
		}

		for (Image image : imageRepository.findByJobGuide(jobGuide)) {

			imageUrlList.add(image.getImageUrl());
			log.info("이미지 URL : " + image.getImageUrl());
		}

		return imageUrlList;
	}

	/**
	 * 이미지 삭제
	 * @param imageId
	 * @return
	 * @auther 근엽
	 */
	@Override
	@Transactional
	public String deleteImage(Integer imageId) {

		Image image = imageRepository.findById(imageId)
			.orElseThrow(() -> new ImageNotFoundException("해당하는 이미지가 없습니다."));

		// awsS3Util.deleteFile(image.getImageUrl());
		imageRepository.delete(image);

		return "이미지가 삭제되었습니다.";
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
