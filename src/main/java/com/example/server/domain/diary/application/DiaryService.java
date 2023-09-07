package com.example.server.domain.diary.application;

import static com.example.server.global.exception.ErrorCode.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.domain.diary.api.dto.DiarySaveRequest;
import com.example.server.domain.diary.api.dto.DiaryUpdateRequest;
import com.example.server.domain.diary.model.Diary;
import com.example.server.domain.diary.model.DiaryStamp;
import com.example.server.domain.diary.model.constants.Category;
import com.example.server.domain.diary.persistence.DiaryRepository;
import com.example.server.domain.image.model.Image;
import com.example.server.domain.member.model.Member;
import com.example.server.domain.member.persistence.MemberQueryRepository;
import com.example.server.domain.pet.model.Pet;
import com.example.server.domain.pet.persistence.PetQueryRepository;
import com.example.server.domain.stamp.model.Stamp;
import com.example.server.domain.stamp.model.constants.StampType;
import com.example.server.domain.stamp.persistence.StampQueryRepository;
import com.example.server.global.exception.BusinessException;
import com.example.server.global.util.S3Uploader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryService {

	private final MemberQueryRepository memberQueryRepository;
	private final PetQueryRepository petQueryRepository;
	private final DiaryRepository diaryRepository;
	private final StampQueryRepository stampQueryRepository;
	private final S3Uploader s3Uploader;
	static final int IMAGE_LIST_SIZE = 5;
	static final int MINIMUM_STAMP_LIST_SIZE = 1;
	static final int MAXIMUM_STAMP_LIST_SIZE = 5;
	static final String DIARY_DIR_NAME = "Diary";

	@Transactional
	public Diary createDiaryExceptForImage(
		String username,
		DiarySaveRequest diarySaveRequest
	) {
		Member member = memberQueryRepository.findByProviderId(username)
			.orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
		Pet pet = petQueryRepository.findPetByIdAndOwner(diarySaveRequest.getPetId(), member)
			.orElseThrow(() -> new BusinessException(PET_NOT_FOUND));
		List<Stamp> stamps = getStamps(diarySaveRequest.getStamps());

		List<DiaryStamp> diaryStamps = generateDiaryStamps(stamps);
		Diary diary = generateDiary(diarySaveRequest, member, pet, diaryStamps);

		diaryRepository.save(diary);

		return diary;
	}

	@Transactional
	public Diary uploadImage(
		Long diaryId,
		String username,
		List<MultipartFile> multipartFiles
	) throws IOException {
		Member member = memberQueryRepository.findByProviderId(username)
			.orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
		Diary diary = diaryRepository.findByIdAndAuthor(diaryId, member)
			.orElseThrow(() -> new BusinessException(DIARY_NOT_FOUND));

		uploadImages(multipartFiles, diary);

		return diary;
	}

	@Transactional
	public Diary updateDiary(
		Long diaryId,
		String username,
		DiaryUpdateRequest diaryUpdateRequest
	) {
		Member member = memberQueryRepository.findByProviderId(username)
			.orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
		Diary diary = diaryRepository.findByIdAndAuthor(diaryId, member)
			.orElseThrow(() -> new BusinessException(DIARY_NOT_FOUND));
		if (diary.getIsDeleted()) {
			throw new BusinessException(DIARY_NOT_FOUND);
		}
		if (diaryUpdateRequest.getPetId() != null) {
			Pet pet = petQueryRepository.findPetByIdAndOwner(diaryUpdateRequest.getPetId(), member)
				.orElseThrow(() -> new BusinessException(PET_NOT_FOUND));
			diary.updatePet(pet);
		}
		if (!diaryUpdateRequest.getStamps().isEmpty()) { // 새로 요청하지 않으면(null 이면) 그대로 유지
			List<DiaryStamp> newDiaryStamps = updateStamp(diaryUpdateRequest, diary);
			diary.updateDiary(diaryUpdateRequest, newDiaryStamps);
		}

		return diary;
	}

	private List<DiaryStamp> updateStamp(DiaryUpdateRequest diaryUpdateRequest, Diary diary) {
		List<DiaryStamp> oldDiaryStamps = diary.getDiaryStamps();

		// 기존 스탬프 제거
		if (diaryUpdateRequest.getStamps() != null) { // 새로 요청하지 않으면(null 이면) 그대로 유지
			for (DiaryStamp oldDiaryStamp : oldDiaryStamps) {
				oldDiaryStamp.removeDiaryStamp();
			}
		}

		// 새로운 스탬프 생성
		List<Stamp> stamps = getStamps(diaryUpdateRequest.getStamps());
		List<DiaryStamp> diaryStamps = generateDiaryStamps(stamps);

		return diaryStamps;
	}

	private List<Stamp> getStamps(List<String> stampList) {
		List<Stamp> stamps = stampList
			.stream()
			.map(s -> stampQueryRepository.findByStampType(StampType.toEnum(s))
				.orElseThrow(() -> new BusinessException(STAMP_INVALID))
			)
			.collect(Collectors.toList());
		if (stamps.size() < MINIMUM_STAMP_LIST_SIZE) {
			throw new BusinessException(STAMP_LIST_SIZE_TOO_SHORT);
		}
		// else if (stamps.size() > STAMP_LIST_SIZE) {
		// 	throw new BusinessException(STAMP_LIST_SIZE_ERROR);
		// }
		return stamps;
	}

	private List<DiaryStamp> generateDiaryStamps(List<Stamp> stamps) {
		return stamps.stream()
			.map(DiaryStamp::of)
			.collect(Collectors.toList());
	}

	private static Diary generateDiary(
		DiarySaveRequest diarySaveRequest,
		Member member,
		Pet pet,
		List<DiaryStamp> diaryStamps
	) {
		return Diary.of(
			pet,
			resolveCategory(diarySaveRequest.getCategory()),
			member,
			diarySaveRequest.getContent(),
			diaryStamps,
			diarySaveRequest.getIsPublic()
		);
	}

	private static Category resolveCategory(String category) {
		return Category.toEnum(category);
	}

	private void uploadImages(List<MultipartFile> multipartFiles, Diary diary) throws IOException {
		if (multipartFiles.size() > IMAGE_LIST_SIZE) {
			throw new BusinessException(MAX_IMAGE_ATTACHMENTS_EXCEEDED);
		}
		List<Image> images = s3Uploader.uploadMultiImages(multipartFiles, DIARY_DIR_NAME);
		diary.addImages(images);
	}

	private void deleteImages(Diary diary) {
		List<Image> images = diary.getImages();
		for (Image image : images) {
			s3Uploader.deleteMultiImages(image);
			image.deleteImage();
		}
	}
}
