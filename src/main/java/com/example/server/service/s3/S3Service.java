package com.example.server.service.s3;

import com.example.server.domain.image.Image;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    Image uploadSingleImage(MultipartFile multipartFile, String dirName) throws IOException;

}
