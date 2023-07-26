package com.example.server.service;

import static com.example.server.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
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

import com.example.server.domain.Image;
import com.example.server.domain.Member;
import com.example.server.dto.MemberDto;
import com.example.server.exception.CustomException;
import com.example.server.repository.MemberRepository;
import com.example.server.util.S3Uploader;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

	@Mock
	MemberRepository memberRepository;

	@InjectMocks
	MemberServiceImpl memberService;

	@Mock
	S3Uploader s3Uploader;

	private Member createMember() {
		return Member.builder()
			.id(1L)
			.username("test")
			.build();
	}

	private MemberDto.ProfileSaveRequest createProfileSaveRequest() {
		return MemberDto.ProfileSaveRequest.builder()
			.nickname("테스트닉네임")
			.build();
	}

	private MemberDto.ProfileSaveRequest createBlankedProfileSaveRequest() {
		return MemberDto.ProfileSaveRequest.builder()
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
		MemberDto.ProfileSaveRequest profileSaveRequest = createProfileSaveRequest();
		when(memberRepository.findByProviderId(member.getUsername())).thenReturn(Optional.of(member));
		when(memberRepository.existsByNickname(profileSaveRequest.getNickname())).thenReturn(false);

		memberService.updateProfile(profileSaveRequest, member.getUsername());

		assertThat(member.getNickname()).isEqualTo(profileSaveRequest.getNickname());
		verify(memberRepository, atLeastOnce()).findByProviderId(member.getUsername());
	}

	@Test
	public void passed_요청필드가_비어있다면_수정하지_않는다() {
		Member member = createMember();
		MemberDto.ProfileSaveRequest blankedProfileSaveRequest = createBlankedProfileSaveRequest();
		when(memberRepository.findByProviderId(member.getUsername())).thenReturn(Optional.of(member));

		memberService.updateProfile(blankedProfileSaveRequest, member.getUsername());

		assertThat(member.getNickname()).isEqualTo(null);
		verify(memberRepository, atLeastOnce()).findByProviderId(member.getUsername());
	}

	@Test
	public void failed_회원을_찾을_수_없는_경우_예외가_발생한다() {
		MemberDto.ProfileSaveRequest profileSaveRequest = createProfileSaveRequest();
		// doThrow(new CustomException(MEMBER_NOT_FOUND)).when(memberRepository)
		// 	.findByProviderId("stranger");

		assertThatThrownBy(() -> memberService.updateProfile(profileSaveRequest, "stranger"))
			.isInstanceOf(CustomException.class);

		verify(memberRepository, atLeastOnce()).findByProviderId("stranger");
		verify(memberRepository, description(MEMBER_NOT_FOUND.getMessage())).findByProviderId("stranger");
	}

	@Test
	void pssed_프로필_이미지를_s3에_업로드한다() throws IOException {
		Image expected = createImage();
		Member member = createMember();
		String dirName = "Test";

		FileInputStream fileInputStream = new FileInputStream("src/test/resources/images/" + expected.getFileName());
		MockMultipartFile mockMultipartFile = new MockMultipartFile("test_img", expected.getFileName(),
			"png", fileInputStream);

		when(memberRepository.findByProviderId(member.getUsername())).thenReturn(Optional.of(member));
		when(s3Uploader.uploadSingleImage(mockMultipartFile, dirName)).thenReturn(expected);
		memberService.uploadProfileImage(member.getUsername(), mockMultipartFile, dirName);

		assertAll(
			() -> assertThat(member.getProfileImage()).isEqualTo(expected)
		);
	}
}
