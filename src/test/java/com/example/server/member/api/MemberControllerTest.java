package com.example.server.member.api;

import static com.example.server.global.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.*;

import java.io.FileInputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.server.domain.image.model.Image;
import com.example.server.domain.member.api.MemberController;
import com.example.server.domain.member.api.dto.MemberProfileSaveRequest;
import com.example.server.domain.member.application.MemberService;
import com.example.server.global.exception.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(MemberControllerTest.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(MemberController.class)
class MemberControllerTest {

	// @Autowired
	@MockBean
	private MemberService memberService;
	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void setup(WebApplicationContext webApplicationContext) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.apply(springSecurity())
			.apply(sharedHttpSession())
			.build();
	}

	private MemberProfileSaveRequest createMemberProfileSaveRequest() {
		return MemberProfileSaveRequest.builder()
			.nickname("변경할닉네임")
			.build();
	}

	private Image createImage() {
		return Image.builder()
			.fileName("test_img.png")
			.imgUrl("test_url")
			.build();
	}

	@Test
	@WithMockUser(username = "test_user")
	@DisplayName("passed_사용자_프로필_정보(닉네임)를_변경할_수_있다")
	void passed_사용자_프로필_정보를_변경할_수_있다() throws Exception {
		MemberProfileSaveRequest requestDto = createMemberProfileSaveRequest();

		doNothing().when(memberService).updateProfile(requestDto, "test_user");

		mockMvc.perform(patch("/member/profile")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectToJsonString(requestDto))
				.with(csrf())
			)
			.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "test_user")
	@DisplayName("failed_존재하지_않는_사용자는_프로필_정보(닉네임)를_변경할_수_없다")
	void failed_존재하지_않는_사용자는_프로필_정보를_변경할_수_없다() throws Exception {
		MemberProfileSaveRequest requestDto = createMemberProfileSaveRequest();

		doThrow(new BusinessException(MEMBER_NOT_FOUND)).when(memberService).updateProfile(requestDto, "test_user");

		BusinessException exception = assertThrows(BusinessException.class, () -> {
			mockMvc.perform(patch("/member/profile")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectToJsonString(requestDto))
				.with(csrf())
			);
		});
		assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());

		// mockMvc.perform(patch("/member/profile")
		// 		.contentType(MediaType.APPLICATION_JSON)
		// 		.content(objectToJsonString(requestDto))
		// 		.with(csrf())
		// 	)
		// 	.andExpect(status().is4xxClientError()) // is4xxClientError()
		// 	.andExpect(result -> assertTrue(result.getResolvedException() instanceof BusinessException));
	}

	// @Test
	// @WithMockUser(username = "test_user")
	// @DisplayName("failed_이미_사용중인_닉네임으로는_변경할_수_없다")
	// void failed_이미_사용중인_닉네임으로는_변경할_수_없다() throws BusinessException {
	// }

	@Test
	@WithMockUser(username = "test_user")
	@DisplayName("passed_프로필_이미지를_업로드_할_수_있다")
	void passed_프로필_이미지를_업로드_할_수_있다() throws Exception {
		Image expected = createImage();
		FileInputStream fileInputStream = new FileInputStream("src/test/resources/images/" + expected.getFileName());
		MockMultipartFile mockMultipartFile = new MockMultipartFile("multipartFile", expected.getFileName(),
			"image/png", fileInputStream);

		doNothing().when(memberService).uploadProfileImage("test_user", mockMultipartFile, "Test");

		mockMvc.perform(MockMvcRequestBuilders.multipart("/member/profile/images")
				.file(mockMultipartFile)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
			)
			.andExpect(status().isOk());
	}

	private String objectToJsonString(Object object) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
