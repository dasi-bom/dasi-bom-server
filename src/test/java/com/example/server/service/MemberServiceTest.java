package com.example.server.service;

import static com.example.server.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.server.domain.Member;
import com.example.server.dto.MemberDto;
import com.example.server.exception.CustomException;
import com.example.server.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

	@Mock
	S3Service s3Service;
	@Mock
	MemberRepository memberRepository;

	@InjectMocks
	MemberServiceImpl memberService;

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

	@Test
	public void passed_프로필_등록() {
		Member member = createMember();
		MemberDto.ProfileSaveRequest profileSaveRequest = createProfileSaveRequest();
		when(memberRepository.findByProviderId(member.getUsername())).thenReturn(Optional.of(member));

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
	public void failed_회원을_찾을_수_없음() {
		MemberDto.ProfileSaveRequest profileSaveRequest = createProfileSaveRequest();
		// doThrow(new CustomException(MEMBER_NOT_FOUND)).when(memberRepository)
		// 	.findByProviderId("stranger");

		assertThatThrownBy(() -> memberService.updateProfile(profileSaveRequest, "stranger"))
			.isInstanceOf(CustomException.class);

		verify(memberRepository, atLeastOnce()).findByProviderId("stranger");
		verify(memberRepository, description(MEMBER_NOT_FOUND.getMessage())).findByProviderId("stranger");
	}
}
