package com.example.server.domain.folder.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.server.domain.diary.api.assembler.DiaryResponseAssembler;
import com.example.server.domain.diary.api.dto.DiaryResponse;
import com.example.server.domain.diary.model.Diary;
import com.example.server.domain.folder.api.dto.FolderCreateRequest;
import com.example.server.domain.folder.api.dto.FolderResponse;
import com.example.server.domain.folder.api.dto.FolderUpdateRequest;
import com.example.server.domain.folder.model.Folder;
import com.example.server.domain.folder.persistence.FolderRepository;
import com.example.server.domain.member.model.Member;
import com.example.server.domain.member.persistence.MemberRepository;
import com.example.server.global.exception.BusinessException;
import com.example.server.global.exception.errorcode.FolderErrorCode;
import com.example.server.global.exception.errorcode.MemberErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FolderService {
	private final MemberRepository memberRepository;
	private final FolderRepository folderRepository;
	private final DiaryResponseAssembler diaryResponseAssembler;

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

	public FolderResponse updateFolder(
		Long folderId,
		String username,
		FolderUpdateRequest folderUpdateRequest
	) {
		Member owner = memberRepository.findByProviderId(username)
			.orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
		Folder folder = folderRepository.findById(folderId)
			.orElseThrow(() -> new BusinessException(FolderErrorCode.FOLDER_NOT_FOUND));
		if (!folder.getOwner().equals(owner)) {
			throw new BusinessException(FolderErrorCode.FOLDER_CANNOT_ACCESS);
		}
		folder.updateFolderName(folderUpdateRequest.getName());
		return FolderResponse.builder()
			.id(folder.getId())
			.name(folder.getName())
			.build();
	}

	public List<DiaryResponse> fetchFolder(
		String username,
		Long folderId
	) {
		Member owner = memberRepository.findByProviderId(username)
			.orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
		Folder folder = folderRepository.findById(folderId)
			.orElseThrow(() -> new BusinessException(FolderErrorCode.FOLDER_NOT_FOUND));
		if (!folder.getOwner().equals(owner)) {
			throw new BusinessException(FolderErrorCode.FOLDER_CANNOT_ACCESS);
		}

		List<Diary> diaryList = folder.getDiaryList();
		return diaryList.stream()
			.map(diaryResponseAssembler::toResponse)
			.collect(Collectors.toList());
	}
}
