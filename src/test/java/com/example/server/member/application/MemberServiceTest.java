package com.example.server.member.application;

import static com.example.server.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.example.server.domain.image.model.Image;
import com.example.server.domain.member.api.dto.MemberProfileSaveRequest;
import com.example.server.domain.member.application.MemberService;
import com.example.server.domain.member.model.Member;
import com.example.server.domain.member.persistence.MemberQueryRepository;
import com.example.server.global.exception.BusinessException;
import com.example.server.global.util.S3Uploader;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

	@Mock
	MemberQueryRepository memberQueryRepository;

	@InjectMocks
	MemberService memberService;

	@Mock
	S3Uploader s3Uploader;

	private Member createMember() {
		return Member.builder()
			.id(1L)
			.username("test")
			.build();
	}

	private MemberProfileSaveRequest createProfileSaveRequest() {
		return MemberProfileSaveRequest.builder()
			.nickname("테스트닉네임")
			.build();
	}

	private MemberProfileSaveRequest createBlankedProfileSaveRequest() {
		return MemberProfileSaveRequest.builder()
			.build();
	}

	private Image createImage() {
		return Image.builder()
			.fileName("test_img.png")
			.imgUrl("test_url")
			.build();
	}

	@Test
	public void passed_닉네임을_포함한_프로필_정보를_등록한다() {
		Member member = createMember();
		MemberProfileSaveRequest profileSaveRequest = createProfileSaveRequest();
		when(memberQueryRepository.findByProviderId(member.getUsername())).thenReturn(Optional.of(member));
		when(memberQueryRepository.existsByNickname(profileSaveRequest.getNickname())).thenReturn(false);

		memberService.updateProfile(profileSaveRequest, member.getUsername());

		assertThat(member.getNickname()).isEqualTo(profileSaveRequest.getNickname());
		verify(memberQueryRepository, atLeastOnce()).findByProviderId(member.getUsername());
	}

	@Test
	public void passed_요청필드가_비어있다면_수정하지_않는다() {
		Member member = createMember();
		MemberProfileSaveRequest blankedProfileSaveRequest = createBlankedProfileSaveRequest();
		when(memberQueryRepository.findByProviderId(member.getUsername())).thenReturn(Optional.of(member));

		memberService.updateProfile(blankedProfileSaveRequest, member.getUsername());

		assertThat(member.getNickname()).isEqualTo(null);
		verify(memberQueryRepository, atLeastOnce()).findByProviderId(member.getUsername());
	}

	@Test
	public void failed_회원을_찾을_수_없는_경우_예외가_발생한다() {
		MemberProfileSaveRequest profileSaveRequest = createProfileSaveRequest();

		assertThatThrownBy(() -> memberService.updateProfile(profileSaveRequest, "stranger"))
			.isInstanceOf(BusinessException.class);

		verify(memberQueryRepository, atLeastOnce()).findByProviderId("stranger");
		verify(memberQueryRepository, description(MEMBER_NOT_FOUND.getMessage())).findByProviderId("stranger");
	}

	// @Test
	// void passed_프로필_이미지를_s3에_업로드한다() throws IOException {
	// 	Image expected = createImage();
	// 	Member member = createMember();
	// 	String dirName = "Test";
	//
	// 	FileInputStream fileInputStream = new FileInputStream("src/test/resources/images/" + expected.getFileName());
	// 	MockMultipartFile mockMultipartFile = new MockMultipartFile("test_img", expected.getFileName(),
	// 		"png", fileInputStream);
	//
	// 	when(memberQueryRepository.findByProviderId(member.getUsername())).thenReturn(Optional.of(member));
	// 	when(s3Uploader.uploadSingleImage(mockMultipartFile, dirName)).thenReturn(expected);
	// 	memberService.uploadProfileImage(member.getUsername(), mockMultipartFile);
	//
	// 	assertAll(
	// 		() -> assertThat(member.getProfileImage()).isEqualTo(expected)
	// 	);
	// }

	@Test
	void failed_IOException이_발생하여_이미지를_업로드할_수_없다() throws IOException {
		Image expected = createImage();
		String dirName = "Test";

		FileInputStream fileInputStream = new FileInputStream("src/test/resources/images/" + expected.getFileName());
		MockMultipartFile mockMultipartFile = new MockMultipartFile("test_img", expected.getFileName(),
			"png", fileInputStream);

		when(s3Uploader.uploadSingleImage(mockMultipartFile, dirName)).thenThrow(IOException.class);

		assertThatThrownBy(() -> s3Uploader.uploadSingleImage(mockMultipartFile, dirName))
			.isInstanceOf(IOException.class);
	}
}
