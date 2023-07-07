package com.example.server.service;

import com.example.server.domain.Image;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    Image uploadSingleImage(MultipartFile multipartFile, String dirName) throws IOException;

}
