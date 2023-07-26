// package com.example.server.service;
//
// import static org.assertj.core.api.Assertions.*;
// import static org.mockito.Mockito.*;
//
// import java.io.FileInputStream;
// import java.io.IOException;
// import java.net.URL;
//
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.mock.web.MockMultipartFile;
// import org.springframework.test.util.ReflectionTestUtils;
//
// import com.example.server.domain.Image;
//
// @ExtendWith(MockitoExtension.class)
// public class S3ServiceTest {
//
// 	@InjectMocks
// 	S3ServiceImpl s3Service;
// 	// @InjectMocks
// 	// S3Mock s3Mock;
//
// 	private Image createImage() {
// 		return Image.builder()
// 			.imgUrl("test_url")
// 			.fileName("mock1.png")
// 			.build();
// 	}
//
//
// 	// @Test
// 	// public void test() throws IOException {
// 	// 	// String expected = "mock1.png";
// 	// 	Image expectedImage = createImage();
// 	// 	MockMultipartFile mockMultipartFile = new MockMultipartFile("file", expectedImage.getFileName(),
// 	// 		"image/png", "test data".getBytes());
// 	// 	// Image image = s3Service.uploadSingleImage(mockMultipartFile, "static");
// 	//
// 	// 	when(s3Service.uploadSingleImage(mockMultipartFile, "Test")).thenReturn(expectedImage);
// 	//
// 	// 	// UploadedImageDto uploadedImageDto = s3ImageUploader.upload(mockMultipartFile, "static");
// 	//
// 	// 	// assertThat(image.getImageExtension()).isEqualTo("png");
// 	// 	assertThat(image.getFileName()).isEqualTo(expectedImage.getFileName());
// 	// }
// 	// @After
// 	// public void shutdownMockS3() {
// 	// 	s3Mock.stop();
// 	// }
//
// 	// @Mock
// 	// ImageRepository imageRepository;
// 	//
// 	// // @InjectMocks
// 	// @MockBean
// 	// S3ServiceImpl S3Service;
// 	//
// 	// private Image createImage() {
// 	// 	return Image.builder()
// 	// 		.imgUrl("test_url")
// 	// 		.fileName("test_file")
// 	// 		.build();
// 	// }
// 	//
// 	// @Test
// 	// public void passed_이미지_업로드() throws IOException {
// 	// 	Image image = createImage();
// 	// 	String expected = "test_img.jpg";
// 	// 	FileInputStream fileInputStream = new FileInputStream("src/test/resources/images/" + expected);
// 	// 	MockMultipartFile mockMultipartFile = new MockMultipartFile("test_img", expected, "jpg", fileInputStream);
// 	//
// 	// 	when(S3Service.uploadSingleImage(mockMultipartFile, "Test")).thenReturn(image);
// 	//
// 	// 	Member actual = memberRepository.findByEmail(member.getEmail())
// 	// 		.orElseThrow();
// 	// 	assertThat(actual.getImgUrl()).isEqualTo(expected);
// 	// }
//
// }
