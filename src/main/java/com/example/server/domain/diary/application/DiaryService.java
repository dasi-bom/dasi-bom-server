package com.example.server.domain.diary.application;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.domain.challenge.model.Challenge;
import com.example.server.domain.challenge.persistence.ChallengeRepository;
import com.example.server.domain.diary.api.dto.DiaryBriefResponse;
import com.example.server.domain.diary.api.dto.DiarySaveRequest;
import com.example.server.domain.diary.model.Diary;
import com.example.server.domain.diary.model.DiaryStamp;
import com.example.server.domain.diary.model.condition.ReadCondition;
import com.example.server.domain.diary.persistence.DiaryRepository;
import com.example.server.domain.image.model.Image;
import com.example.server.domain.member.model.Member;
import com.example.server.domain.member.persistence.MemberRepository;
import com.example.server.domain.pet.model.Pet;
import com.example.server.domain.pet.persistence.PetRepository;
import com.example.server.domain.stamp.model.Stamp;
import com.example.server.domain.stamp.persistence.StampRepository;
import com.example.server.global.exception.BusinessException;
import com.example.server.global.exception.errorcode.ChallengeErrorCode;
import com.example.server.global.exception.errorcode.DiaryErrorCode;
import com.example.server.global.exception.errorcode.MemberErrorCode;
import com.example.server.global.exception.errorcode.PetErrorCode;
import com.example.server.global.exception.errorcode.StampErrorCode;
import com.example.server.global.util.S3Uploader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryService {

	static final int IMAGE_LIST_SIZE = 5;
	static final int MINIMUM_STAMP_LIST_SIZE = 1;
	static final int MAXIMUM_STAMP_LIST_SIZE = 5;
	static final String DIARY_DIR_NAME = "Diary";
	private final MemberRepository memberRepository;
	private final PetRepository petRepository;
	private final DiaryRepository diaryRepository;
	private final StampRepository stampRepository;
	private final ChallengeRepository challengeRepository;
	private final S3Uploader s3Uploader;

	@Transactional
	public Diary createDiaryExceptForImage(
		String username,
		DiarySaveRequest diarySaveRequest
	) {
		Member member = memberRepository.findByProviderId(username)
			.orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
		Pet pet = petRepository.findPetByIdAndOwner(diarySaveRequest.getPetId(), member)
			.orElseThrow(() -> new BusinessException(PetErrorCode.PET_NOT_FOUND));
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
		Member member = memberRepository.findByProviderId(username)
			.orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
		Diary diary = diaryRepository.findByIdAndAuthor(diaryId, member)
			.orElseThrow(() -> new BusinessException(DiaryErrorCode.DIARY_NOT_FOUND));

		uploadImages(multipartFiles, diary);

		return diary;
	}

	@Transactional
	public Diary updateDiary(
		Long diaryId,
		String username,
		DiarySaveRequest diarySaveRequest
	) {
		Member member = memberRepository.findByProviderId(username)
			.orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
		Diary diary = diaryRepository.findByIdAndAuthor(diaryId, member)
			.orElseThrow(() -> new BusinessException(DiaryErrorCode.DIARY_NOT_FOUND));
		if (diary.getIsDeleted()) {
			throw new BusinessException(DiaryErrorCode.DIARY_NOT_FOUND);
		}

		Pet pet = petRepository.findPetByIdAndOwner(diarySaveRequest.getPetId(), member)
			.orElseThrow(() -> new BusinessException(PetErrorCode.PET_NOT_FOUND));
		diary.updatePet(pet);

		Challenge challenge = null;
		if (diarySaveRequest.getChallengeId() != null) {
			challenge = challengeRepository.findById(diarySaveRequest.getChallengeId())
				.orElseThrow(() -> new BusinessException(ChallengeErrorCode.CHALLENGE_INVALID));
		}
		diary.updateChallenge(challenge);

		List<DiaryStamp> newDiaryStamps = updateStamp(diarySaveRequest, diary);
		diary.updateDiary(diarySaveRequest, newDiaryStamps);

		return diary;
	}

	@Transactional
	public void deleteDiary(
		Long diaryId,
		String username
	) {
		Member member = memberRepository.findByProviderId(username)
			.orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
		Diary diary = diaryRepository.findByIdAndAuthor(diaryId, member)
			.orElseThrow(() -> new BusinessException(DiaryErrorCode.DIARY_NOT_FOUND));
		if (diary.getIsDeleted()) {
			throw new BusinessException(DiaryErrorCode.DIARY_NOT_FOUND);
		}
		diary.deleteDiary();
	}

	public Slice<DiaryBriefResponse> getAll(
		Long cursor,
		ReadCondition condition,
		Pageable pageRequest
	) {
		return diaryRepository.getDiaryBriefScroll(cursor, condition, pageRequest);
	}

	private List<DiaryStamp> updateStamp(DiarySaveRequest diarySaveRequest, Diary diary) {
		List<DiaryStamp> oldDiaryStamps = diary.getDiaryStamps();

		// 기존 스탬프 제거
		if (diarySaveRequest.getStamps() != null) { // 새로 요청하지 않으면(null 이면) 그대로 유지
			for (DiaryStamp oldDiaryStamp : oldDiaryStamps) {
				oldDiaryStamp.removeDiaryStamp();
			}
		}

		// 새로운 스탬프 생성
		List<Stamp> stamps = getStamps(diarySaveRequest.getStamps());
		List<DiaryStamp> diaryStamps = generateDiaryStamps(stamps);

		return diaryStamps;
	}

	private List<Stamp> getStamps(List<Long> stampList) {
		List<Stamp> stamps = stampList.stream()
			.map(stampId -> stampRepository.findById(stampId)
				.orElseThrow(() -> new BusinessException(StampErrorCode.STAMP_INVALID))
			)
			.collect(Collectors.toList());
		if (stamps.size() < MINIMUM_STAMP_LIST_SIZE) {
			throw new BusinessException(StampErrorCode.STAMP_LIST_SIZE_TOO_SHORT);
		}
		// else if (stamps.size() > STAMP_LIST_SIZE) {
		// 	throw new BusinessException(STAMP_LIST_SIZE_ERROR);
		// }
		return stamps;
	}

	private List<DiaryStamp> generateDiaryStamps(List<Stamp> stamps) {
		return stamps.stream()
			.map(stamp -> DiaryStamp.builder()
				.stamp(stamp)
				.build())
			.collect(Collectors.toList());
	}

	private Diary generateDiary(
		DiarySaveRequest diarySaveRequest,
		Member member,
		Pet pet,
		List<DiaryStamp> diaryStamps
	) {
		Challenge challenge = null;
		if (diarySaveRequest.getChallengeId() != null) {
			challenge = challengeRepository.findById(diarySaveRequest.getChallengeId())
				.orElseThrow(() -> new BusinessException(ChallengeErrorCode.CHALLENGE_INVALID));
		}
		return Diary.builder()
			.pet(pet)
			.challenge(challenge)
			.author(member)
			.content(diarySaveRequest.getContent())
			.diaryStamps(diaryStamps)
			.isPublic(diarySaveRequest.getIsPublic())
			.build();
	}

	private void uploadImages(List<MultipartFile> multipartFiles, Diary diary) throws IOException {
		if (multipartFiles.size() > IMAGE_LIST_SIZE) {
			throw new BusinessException(DiaryErrorCode.MAX_IMAGE_ATTACHMENTS_EXCEEDED);
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
