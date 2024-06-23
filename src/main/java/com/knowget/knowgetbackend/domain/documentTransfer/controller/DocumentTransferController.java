package com.knowget.knowgetbackend.domain.documentTransfer.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.knowget.knowgetbackend.domain.documentTransfer.service.DocumentTransferService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/api/v1/document-transfer")
@RequiredArgsConstructor
public class DocumentTransferController {

	private final DocumentTransferService documentTransferService;

	/**
	 * 문서 업로드
	 * @param jobGuideId
	 * @param file
	 * @return imageUrl
	 * @auther 근엽
	 */
	// @PostMapping("{jobGuideId}/upload")
	// public String uploadFile(@PathVariable Integer jobGuideId,
	// 	@RequestParam(name = "file") MultipartFile file
	// ) {
	//
	// 	String documentUrl = documentTransferService.uploadFile(file, jobGuideId);
	//
	// 	return documentUrl;
	// }

	/**
	 * 문서 다중 업로드
	 * @param jobGuideId
	 * @param files
	 * @return imageUrls
	 * @auther 근엽
	 */
	@PostMapping("{jobGuideId}/uploads")
	public List<String> uploadFiles(@PathVariable Integer jobGuideId,
		@RequestParam(value = "files") List<MultipartFile> files
	) {

		List<String> documentUrls = documentTransferService.uploadFiles(files, jobGuideId);

		return documentUrls;
	}

	/**
	 * 문서 삭제
	 * @param documentId
	 * @return imageUrl
	 * @auther 근엽
	 */
	@DeleteMapping("{documentId}/delete")
	public String deleteDocument(@PathVariable Integer documentId) {
		String documentUrl = documentTransferService.deleteDocument(documentId);

		return documentUrl;
	}

}
