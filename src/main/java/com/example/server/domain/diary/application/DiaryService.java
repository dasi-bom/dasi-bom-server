package com.example.server.domain.diary.application;

import static com.example.server.global.exception.ErrorCode.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.domain.diary.api.dto.DiarySaveRequest;
import com.example.server.domain.diary.model.Diary;
import com.example.server.domain.diary.model.DiaryStamp;
import com.example.server.domain.diary.model.constants.Category;
import com.example.server.domain.diary.model.constants.ChallengeTopic;
import com.example.server.domain.diary.persistence.DiaryRepository;
import com.example.server.domain.image.model.Image;
import com.example.server.domain.member.application.MemberFindService;
import com.example.server.domain.member.model.Member;
import com.example.server.domain.stamp.model.Stamp;
import com.example.server.domain.stamp.model.constants.StampType;
import com.example.server.domain.stamp.persistence.StampRepository;
import com.example.server.global.exception.BusinessException;
import com.example.server.global.util.S3Uploader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryService {

	private final MemberFindService memberFindService;
	private final DiaryRepository diaryRepository;
	private final StampRepository stampRepository;
	private final S3Uploader s3Uploader;

	@Transactional
	public void createDiary(String username,
		DiarySaveRequest diarySaveRequest,
		List<MultipartFile> multipartFiles,
		String dirName
	) throws IOException {
		Member member = memberFindService.findMemberByProviderId(username);
		List<Stamp> stamps = diarySaveRequest.getStamps()
			.stream()
			.map(s -> stampRepository.findByStampType(StampType.toEnum(s))
				.orElseThrow(() -> new BusinessException(STAMP_INVALID)))
			.collect(Collectors.toList());

		// DiaryStamp 생성
		List<DiaryStamp> diaryStampList = new ArrayList<>();
		for (Stamp stamp : stamps) {
			DiaryStamp diaryStamp = DiaryStamp.of(stamp);
			diaryStampList.add(diaryStamp);
		}

		// 일기 생성
		Diary diary = Diary.of(
			Category.toEnum(diarySaveRequest.getCategory()),
			(diarySaveRequest.getChallengeTopic() != null)
				? ChallengeTopic.toEnum(diarySaveRequest.getChallengeTopic()) : null,
			member,
			(diarySaveRequest.getChallengeTopic() != null)
				? diarySaveRequest.getContent() : null,
			diaryStampList,
			diarySaveRequest.getIsPublic()
		);

		if (multipartFiles != null) {
			List<Image> images = s3Uploader.uploadMultiImages(multipartFiles, dirName);
			diary.addImages(images);
		}

		diaryRepository.save(diary);
	}

}
