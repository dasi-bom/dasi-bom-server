package com.example.server.pet.api;

import com.example.server.domain.member.model.Member;
import com.example.server.domain.pet.api.PetProfileController;
import com.example.server.domain.pet.api.dto.PetProfileCreateRequest;
import com.example.server.domain.pet.model.Pet;
import com.example.server.global.base.ControllerTestSupport;
import com.example.server.pet.support.PetInfoFixture;
import com.example.server.pet.support.PetTempProtectedInfoFixture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Import(PetProfileController.class)
@WebMvcTest(PetProfileControllerTest.class)
@DisplayName("[PetController] - MockMVC Test")
class PetProfileControllerTest extends ControllerTestSupport {

    private static final String BASE_PATH = "/pet/profile";

    public Member createMember() {
        return Member.builder()
                .id(1L)
                .username("test")
                .build();
    }

    public Pet createPet() {
        return Pet.builder()
                .id(1L)
                .owner(createMember())
                .profileImage(null)
                .petInfo(PetInfoFixture.VALID_PET.toEntity())
                .petTempProtectedInfo(PetTempProtectedInfoFixture.VALID_PET.toEntity())
                .build();
    }

    @Nested
    @DisplayName("[createPetProfile] 펫 신규 등록 API Test")
    class createPetProfile {

        @Test
        @WithMockUser(username = "test_user")
        @DisplayName("[]")
        void passed_새로운_펫_정보를_등록할_수_있다() throws Exception {
            //given
            PetProfileCreateRequest reqDto = PetInfoFixture.VALID_PET.toRequest();
            Pet pet = createPet();
            given(petService.createProfile(any(), any())).willReturn(pet);

            //when
            mockMvc.perform(post(BASE_PATH)
                            .contentType(APPLICATION_JSON)
                            .content(toJson(reqDto))
                            .with(csrf())
                    )
                    .andExpect(status().isCreated())
                    .andDo(
                            restDocs.document(
                                    requestFields(
                                            fieldWithPath("name").description("동물 이름"),
                                            fieldWithPath("age").description("동물 나이"),
                                            fieldWithPath("sex").description("동물 성별"),
                                            fieldWithPath("bio").description("동물 소개"),
                                            fieldWithPath("type").description("동물 종류"),
                                            fieldWithPath("startTempProtectedDate").description("동물 임시보호 시작일")
                                    )
                            )
                    );
        }
    }

}
