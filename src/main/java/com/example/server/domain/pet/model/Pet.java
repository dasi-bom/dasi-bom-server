package com.example.server.domain.pet.model;

import com.example.server.domain.image.model.Image;
import com.example.server.domain.member.model.Member;
import com.example.server.domain.pet.api.dto.PetProfileUpdateRequest;
import com.example.server.domain.pet.model.constants.PetType;
import com.example.server.domain.pet.model.constants.Sex;
import com.example.server.global.auditing.BaseEntity;
import com.example.server.global.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

import static com.example.server.global.exception.ErrorCode.*;
import static java.util.Objects.isNull;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "pet_tb")
@NoArgsConstructor(access = PROTECTED)
public class Pet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "owner_id")
    private Member owner;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "image_id")
    private Image profileImage;

    @Enumerated(STRING)
    private PetType type;

    private String name;

    @Column(columnDefinition = "TINYINT")
    private Integer age;

    @Enumerated(STRING)
    @Column(nullable = false)
    private Sex sex;

    @Column(columnDefinition = "varchar(300)")
    private String bio;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startTempProtectedDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endTempProtectedDate;

    //== validation constructor ==//
    @Builder
    private Pet(
            final Member owner,
            final PetType type,
            final Image profileImage,
            final String name,
            final Sex sex,
            final LocalDate startTempProtectedDate,
            final Integer age,
            final String bio
    ) {
        validateOwner(owner);
        validateType(type);
        validateName(name);
        validateStartTempProtectedDate(startTempProtectedDate);
        validateAge(age);
        validateBio(bio);
        this.owner = owner;
        this.type = type;
        this.profileImage = profileImage;
        this.name = name;
        this.sex = sex;
        this.startTempProtectedDate = startTempProtectedDate;
        this.age = age;
        this.bio = bio;
    }

    //== static factory method ==//
    public static Pet of(
            final Member owner,
            final PetType type,
            final String name,
            final Sex sex,
            final LocalDate startTempProtectedDate,
            final Integer age,
            final String bio
    ) {
        return Pet.builder()
                .owner(owner)
                .type(type)
                .name(name)
                .sex(sex)
                .startTempProtectedDate(startTempProtectedDate)
                .age(age)
                .bio(bio)
                .build();
    }

    //== validation method ==//
    private void validateOwner(final Member owner) {
        if (isNull(owner)) {
            throw new BusinessException(PET_OWNER_NULL);
        }
    }

    private void validateType(final PetType type) {
        if (isNull(type)) {
            throw new BusinessException(PET_TYPE_NULL);
        }
    }

    private void validateName(final String name) {
        if (isNull(name)) {
            throw new BusinessException(PET_NAME_NULL);
        } else if (name.length() > 20) {
            throw new BusinessException(PET_NAME_TOO_LONG);
        } else if (!name.matches("^[가-힣a-zA-Z]+$")) {
            throw new BusinessException(PET_NAME_INVALID_CHARACTERS);
        }
    }

    private void validateAge(final Integer age) {
        if (age < 1 || age > 100) {
            throw new BusinessException(PET_AGE_INVALID);
        }
    }

    private void validateBio(final String bio) {
        if (!isNull(bio) && bio.length() > 300) {
            throw new BusinessException(PET_BIO_TOO_LONG);
        }
    }

    private void validateStartTempProtectedDate(final LocalDate tempProtectedDate) {
        if (isNull(tempProtectedDate)) {
            throw new BusinessException(PET_TEMP_PROTECTED_SRT_DATE_INVALID);
        } else if (tempProtectedDate.isBefore(
                LocalDate.of(1900, 1, 1))) {
            throw new BusinessException(PET_TEMP_PROTECTED_SRT_DATE_TOO_EARLY);
        } else if (tempProtectedDate.isAfter(LocalDate.now())) {
            throw new BusinessException(PET_TEMP_PROTECTED_SRT_DATE_IN_FUTURE);
        }
    }

    private void validateProfileImage(final Image image) {
        if (isNull(image)) {
            throw new BusinessException(PET_PROFILE_IMAGE_INVALID);
        }
    }

    //==utility method==//
    public void updateProfileImage(Image image) {
        validateProfileImage(image);
        this.profileImage = image;
    }

    public void updateProfile(PetProfileUpdateRequest petReq) {
        validateType(PetType.valueOf(petReq.getType()));
        validateName(petReq.getName());
        validateStartTempProtectedDate(petReq.getStartTempProtectedDate());
        validateAge(petReq.getAge());
        validateBio(petReq.getBio());

        this.type = PetType.valueOf(petReq.getType());
        this.name = petReq.getName();
        this.sex = Sex.valueOf(petReq.getSex());
        this.startTempProtectedDate = petReq.getStartTempProtectedDate();
        this.age = petReq.getAge();
        this.bio = petReq.getBio();
    }
}
