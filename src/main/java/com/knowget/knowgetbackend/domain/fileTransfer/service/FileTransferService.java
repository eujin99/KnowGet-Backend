package com.knowget.knowgetbackend.domain.fileTransfer.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface FileTransferService {
	String uploadFile(MultipartFile file, Integer jobGuideId);

	List<String> uploadFiles(List<MultipartFile> files, Integer jobGuidId);

	String deleteImage(Integer imageId);
}
