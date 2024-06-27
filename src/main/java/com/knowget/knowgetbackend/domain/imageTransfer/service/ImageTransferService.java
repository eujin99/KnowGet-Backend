package com.knowget.knowgetbackend.domain.imageTransfer.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ImageTransferService {
	// String uploadFile(MultipartFile file, Integer jobGuideId);

	List<String> uploadFiles(List<MultipartFile> files, Integer jobGuideId);

	String deleteImage(Integer imageId);

	List<String> getImageUrls(Integer guideId);

	List<String> updateImage(Integer guideId, List<MultipartFile> files);
}
