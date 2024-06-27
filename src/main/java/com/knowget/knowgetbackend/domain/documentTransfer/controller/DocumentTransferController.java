package com.knowget.knowgetbackend.domain.documentTransfer.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.knowget.knowgetbackend.domain.documentTransfer.service.DocumentTransferService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/api/v1/document")
@RequiredArgsConstructor
public class DocumentTransferController {

	private final DocumentTransferService documentTransferService;

	// /**
	//  * 문서 업로드
	//  * @param jobGuideId
	//  * @param file
	//  * @return imageUrl
	//  * @auther 근엽
	//  */
	// @PostMapping("{jobGuideId}/upload")
	// public String uploadFile(@PathVariable Integer jobGuideId,
	// 	@RequestParam(name = "file") MultipartFile file
	// ) {
	//
	// 	String documentUrl = documentTransferService.uploadFile(file, jobGuideId);
	//
	// 	return documentUrl;
	// }

	// /**
	//  * 문서 다중 업로드
	//  *
	//  * @param guideId
	//  * @param files
	//  * @return imageUrls
	//  * @auther 근엽
	//  */
	// @PostMapping("/{guideId}/uploads")
	// public List<String> uploadFiles(@PathVariable Integer guideId,
	// 	@RequestParam(value = "files") List<MultipartFile> files
	// ) {
	//
	// 	List<String> documentUrls = documentTransferService.uploadFiles(files, guideId);
	//
	// 	return documentUrls;
	// }

	/**
	 * 문서 삭제
	 *
	 * @param documentId
	 * @return imageUrl
	 * @auther 근엽
	 */
	@DeleteMapping("/{documentId}/delete")
	public String deleteDocument(@PathVariable Integer documentId) {
		String documentUrl = documentTransferService.deleteDocument(documentId);

		return documentUrl;
	}

	/**
	 * 특정 가이드에 포함된 문서 URL 반환
	 *
	 * @param guideId 가이드 ID
	 * @return imageUrls
	 * @author Jihwan
	 */
	@GetMapping("/{guideId}")
	public ResponseEntity<List<String>> getDocumentUrls(@PathVariable Integer guideId) {
		List<String> documentUrls = documentTransferService.getDocumentUrls(guideId);
		return new ResponseEntity<>(documentUrls, HttpStatus.OK);
	}

	/**
	 * 문서 업데이트
	 *
	 * @param guideId
	 * @param files
	 * @return documentUrls
	 * @auther 근엽
	 */
	@PutMapping("/{guideId}/update")
	public List<String> updateDocument(@PathVariable Integer guideId,
		@RequestParam(value = "files") List<MultipartFile> files
	) {
		List<String> documentUrls = documentTransferService.updateDocument(guideId, files);
		return documentUrls;
	}

}
