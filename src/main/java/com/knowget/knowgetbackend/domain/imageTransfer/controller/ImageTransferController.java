package com.knowget.knowgetbackend.domain.imageTransfer.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.knowget.knowgetbackend.domain.imageTransfer.service.ImageTransferService;
import com.knowget.knowgetbackend.global.config.s3.AwsS3Util;
import com.knowget.knowgetbackend.global.dto.ResultMessageDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/image-transfer")
@RequiredArgsConstructor
public class ImageTransferController {
	private final AwsS3Util awsS3Util;

	private final ImageTransferService imageTransferService;

	// /**
	//  * 이미지 업로드
	//  * @param jobGuidId
	//  * @param file
	//  * @return imageUrl
	//  * @author 근엽
	//  */
	// @PostMapping("{jobGuidId}/upload")
	// public ResponseEntity<ResultMessageDTO> uploadFile(@PathVariable Integer jobGuidId,
	// 	@RequestParam(name = "file") MultipartFile file
	// ) {
	//
	// 	String imageUrl = imageTransferService.uploadFile(file, jobGuidId);
	//
	// 	return new ResponseEntity<>(new ResultMessageDTO(imageUrl), HttpStatus.OK);
	// }

	/**
	 * 이미지 다중 업로드
	 * @param jobGuidId
	 * @param files
	 * @return imageUrls
	 * @auther 근엽
	 */
	@PostMapping("{jobGuidId}/uploads")
	public ResponseEntity<List<String>> uploadFiles(@PathVariable Integer jobGuidId,
		@RequestParam(value = "files") List<MultipartFile> files
	) {

		List<String> imageUrls = imageTransferService.uploadFiles(files, jobGuidId);

		return new ResponseEntity<>(imageUrls, HttpStatus.OK);
	}

	// 다운로드
	// @GetMapping("/download")
	// public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam(value = "image") String image) {
	//
	// 	//  ex. image=https://board-example.s3.ap-northeast-2.amazonaws.com/2b8359b2-de59-4765-8da0-51f5d4e556c3.jpg
	//
	// 	byte[] data = awsS3Util.downloadFile(image);
	// 	ByteArrayResource resource = new ByteArrayResource(data);
	// 	return ResponseEntity
	// 		.ok()
	// 		.contentLength(data.length)
	// 		.header("Content-type", "application/octet-stream")
	// 		.header("Content-disposition", "attachment; filename=\"" + image + "\"")
	// 		.body(resource);
	// }

	/**
	 *	이미지 삭제
	 * @param imageId
	 * @return deleteImage
	 * @auther 근엽
	 */
	@DeleteMapping("{imageId}/delete")
	public ResponseEntity<ResultMessageDTO> deleteFile(@PathVariable Integer imageId) {

		String deleteImage = imageTransferService.deleteImage(imageId);

		return new ResponseEntity<>(new ResultMessageDTO(deleteImage), HttpStatus.OK);
	}

}
