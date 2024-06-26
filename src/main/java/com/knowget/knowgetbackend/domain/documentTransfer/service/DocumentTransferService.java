package com.knowget.knowgetbackend.domain.documentTransfer.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface DocumentTransferService {
	// String uploadFile(MultipartFile file, Integer jobGuideId);

	List<String> uploadFiles(List<MultipartFile> files, Integer guideId);

	String deleteDocument(Integer documentId);

	List<String> getDocumentUrls(Integer guideId);

	List<String> updateDocument(Integer guideId, List<MultipartFile> files);
}
