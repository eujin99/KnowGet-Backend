package com.knowget.knowgetbackend.global.config.s3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsS3Util2 {
	@Value("${bucket2.name}")
	private String bucketName;

	private final AmazonS3 s3Client;

	public String uploadFile(MultipartFile file) {

		if (file == null || file.isEmpty())
			return "";
		// 파일 크기 체크
		if (file.getSize() > 52428800) { // 50MB = 50 * 1024 * 1024
			throw new MaxUploadSizeExceededException(file.getSize());
		}

		File fileObj = convertMultiPartFileToFile(file);
		String originalFilename = file.getOriginalFilename();
		String extension = getFileExtension(originalFilename);
		String fileName = UUID.randomUUID() + "." + extension;

		log.info("uploadFile fileName: {}", fileName);
		s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
		fileObj.delete();
		return s3Client.getUrl(bucketName, fileName).toString();
	}

	public String uploadFiles(List<MultipartFile> files) {
		// 다중 업로드 && 리스트 ","을 기준으로 하나의 문자열 반환
		// files 갯수 0 이면 반환 ""
		if (files == null || files.size() == 0)
			return "";

		StringBuilder mergedUrl = new StringBuilder();
		for (int i = 0; i < files.size(); i++) {
			mergedUrl.append(uploadFile(files.get(i)));
			if (i < files.size() - 1) {
				mergedUrl.append(",");
			}
		}
		log.info("uploadFiles mergedUrl: {}", mergedUrl);
		return mergedUrl.toString();
	}

	public byte[] downloadFile(String document) {

		String filename = document.substring(document.lastIndexOf('/') + 1);

		S3Object s3Object = s3Client.getObject(bucketName, filename);
		S3ObjectInputStream inputStream = s3Object.getObjectContent();
		try {
			byte[] content = IOUtils.toByteArray(inputStream);
			return content;
		} catch (IOException e) {
			// e.printStackTrace();
			throw new IllegalStateException("aws s3 다운로드 error");
		}
	}

	public String deleteFile(String fileName) {
		s3Client.deleteObject(bucketName, fileName);
		return fileName + " removed ...";
	}

	private File convertMultiPartFileToFile(MultipartFile file) {
		File convertedFile = new File(file.getOriginalFilename());
		try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
			fos.write(file.getBytes());
		} catch (IOException e) {
			log.error("Error converting multipartFile to file", e);
		}
		return convertedFile;
	}

	private static String getFileExtension(String originalFileName) {
		return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
	}

}
