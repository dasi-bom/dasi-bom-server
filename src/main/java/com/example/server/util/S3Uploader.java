package com.example.server.util;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.server.domain.image.Image;
import com.example.server.persistence.image.ImageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {

	private final AmazonS3 amazonS3;
	private final ImageRepository imageRepository;
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public Image uploadSingleImage(MultipartFile multipartFile, String dirName) throws IOException {

		String s3FileName = createFileName(multipartFile.getOriginalFilename(), dirName);

		ObjectMetadata objMeta = new ObjectMetadata();
		objMeta.setContentLength(multipartFile.getInputStream().available());
		amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);
		String url = URLDecoder.decode(amazonS3.getUrl(bucket, s3FileName).toString(), "utf-8");

		Image img = new Image();
		img.updateImage(url, s3FileName);

		imageRepository.save(img);

		return img;
	}

	private String createFileName(String originalName, String dirName) {
		return dirName + "/" + UUID.randomUUID() + getFileExtension(originalName);
	}

	private String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

}
