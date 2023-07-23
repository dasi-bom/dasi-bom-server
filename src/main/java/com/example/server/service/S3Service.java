package com.example.server.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.example.server.domain.Image;

public interface S3Service {
	Image uploadSingleImage(MultipartFile multipartFile, String dirName) throws IOException;

}
