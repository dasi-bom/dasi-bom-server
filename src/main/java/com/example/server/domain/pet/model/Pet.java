package com.example.server.domain.pet.model;

import com.example.server.domain.image.model.Image;
import com.example.server.domain.member.model.Member;
import com.example.server.global.auditing.BaseEntity;
import com.example.server.global.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.example.server.global.exception.ErrorCode.PET_OWNER_NULL;
import static java.util.Objects.isNull;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Builder
@Entity
@Getter
@Table(name = "pet_tb")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Pet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "owner_id")
    private Member owner;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "image_id")
    private Image profile;

    @Embedded
    private PetInfo petInfo;

    @Embedded
    private PetTempProtectedInfo petTempProtectedInfo;


    //== validation constructor ==//
    @Builder
    private Pet(
            final Member owner,
            final PetInfo petInfo,
            final PetTempProtectedInfo petTempProtectedInfo
    ) {
        validateOwner(owner);
        this.owner = owner;
        this.petInfo = petInfo;
        this.petTempProtectedInfo = petTempProtectedInfo;
    }

    //== static factory method ==//
    public static Pet of(
            final Member owner,
            final PetInfo petInfo,
            final PetTempProtectedInfo petTempProtectedInfo
    ) {
        return Pet.builder()
                .owner(owner)
                .petInfo(petInfo)
                .petTempProtectedInfo(petTempProtectedInfo)
                .build();
    }

    //== validation method ==//
    private void validateOwner(final Member owner) {
        if (isNull(owner)) {
            throw new BusinessException(PET_OWNER_NULL);
        }
    }
}
