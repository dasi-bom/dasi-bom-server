package com.example.server.domain.diary.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.domain.diary.api.dto.DiarySaveRequest;
import com.example.server.domain.diary.model.Diary;
import com.example.server.domain.diary.model.constants.Category;
import com.example.server.domain.diary.model.constants.ChallengeTopic;
import com.example.server.domain.diary.persistence.DiaryRepository;
import com.example.server.domain.member.application.MemberFindService;
import com.example.server.domain.member.model.Member;
import com.example.server.global.util.S3Uploader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryService {

	private final MemberFindService memberFindService;
	private final DiaryRepository diaryRepository;
	private final S3Uploader s3Uploader;

	@Transactional
	public void createDiary(String username, DiarySaveRequest diarySaveRequest,
		List<MultipartFile> multipartFiles) {

		System.out.println("---------> username = " + username);
		Member member = memberFindService.findMemberByProviderId(username);

		Diary diary = Diary.of(
			Category.toEnum(diarySaveRequest.getCategory()),
			ChallengeTopic.toEnum(diarySaveRequest.getChallengeTopic()),
			diarySaveRequest.getImages(),
			member,
			diarySaveRequest.getContent(),
			diarySaveRequest.getDiaryStamps(),
			diarySaveRequest.getIsPublic()
		);

		// S3 업로드 로직

		diaryRepository.save(diary);
	}

}
