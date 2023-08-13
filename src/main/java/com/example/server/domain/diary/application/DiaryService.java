package com.example.server.domain.diary.application;

import static com.example.server.global.exception.ErrorCode.*;

import java.io.IOException;
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
import com.example.server.domain.pet.application.PetFindService;
import com.example.server.domain.pet.model.Pet;
import com.example.server.domain.stamp.application.StampFindService;
import com.example.server.domain.stamp.model.Stamp;
import com.example.server.domain.stamp.model.constants.StampType;
import com.example.server.global.exception.BusinessException;
import com.example.server.global.util.S3Uploader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryService {

	private final MemberFindService memberFindService;
	private final PetFindService petFindService;
	private final DiaryRepository diaryRepository;
	private final StampFindService stampFindService;
	private final S3Uploader s3Uploader;
	static final int IMAGE_LIST_SIZE = 5;
	static final String DIARY_DIR_NAME = "Diary";

	@Transactional
	public Diary createDiary(
		String username,
		DiarySaveRequest diarySaveRequest,
		List<MultipartFile> multipartFiles
	) throws IOException {
		Member member = memberFindService.findMemberByProviderId(username);
		if (!petFindService.findPetsByOwner(member).contains(diarySaveRequest.getPetId())) {
			throw new BusinessException(PET_NOT_FOUND);
		}
		Pet pet = petFindService.findPetById(diarySaveRequest.getPetId());

		List<Stamp> stamps = diarySaveRequest.getStamps()
			.stream()
			.map(s -> stampFindService.findByStampType(StampType.toEnum(s)))
			.collect(Collectors.toList());
		// todo: 스탬프 개수 제한 확인
		// if (stamps.size() > STAMP_LIST_SIZE) {
		// 	throw new BusinessException(STAMP_LIST_SIZE_ERROR);
		// }

		// DiaryStamp 생성
		List<DiaryStamp> diaryStamps = generateDiaryStamps(stamps);

		// 일기 생성
		Diary diary = Diary.of(
			pet,
			Category.toEnum(diarySaveRequest.getCategory()),
			(diarySaveRequest.getChallengeTopic() != null)
				? ChallengeTopic.toEnum(diarySaveRequest.getChallengeTopic()) : null,
			member,
			(diarySaveRequest.getChallengeTopic() != null)
				? diarySaveRequest.getContent() : null,
			diaryStamps,
			diarySaveRequest.getIsPublic()
		);

		if (multipartFiles != null) {
			if (multipartFiles.size() > IMAGE_LIST_SIZE) {
				throw new BusinessException(MAX_IMAGE_ATTACHMENTS_EXCEEDED);
			}
			List<Image> images = s3Uploader.uploadMultiImages(multipartFiles, DIARY_DIR_NAME);
			diary.addImages(images);
		}

		diaryRepository.save(diary);
		return diary;
	}

	private List<DiaryStamp> generateDiaryStamps(List<Stamp> stamps) {
		return stamps.stream()
			.map(DiaryStamp::of)
			.collect(Collectors.toList());
	}

}
