package com.example.server.domain.folder.application;

import org.springframework.stereotype.Service;

import com.example.server.domain.folder.api.dto.FolderCreateRequest;
import com.example.server.domain.folder.api.dto.FolderResponse;
import com.example.server.domain.folder.model.Folder;
import com.example.server.domain.folder.persistence.FolderRepository;
import com.example.server.domain.member.model.Member;
import com.example.server.domain.member.persistence.MemberRepository;
import com.example.server.global.exception.BusinessException;
import com.example.server.global.exception.errorcode.MemberErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FolderService {
	private final MemberRepository memberRepository;
	private final FolderRepository folderRepository;

	public FolderResponse createFolder(
		String username,
		FolderCreateRequest folderCreateRequest
	) {
		Member owner = memberRepository.findByProviderId(username)
			.orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
		Folder folder = Folder.builder()
			.name(folderCreateRequest.getName())
			.cnt(0)
			.owner(owner)
			.build();
		Folder savedFolder = folderRepository.save(folder);
		return FolderResponse.builder()
			.id(savedFolder.getId())
			.name(savedFolder.getName())
			.build();
	}
}
