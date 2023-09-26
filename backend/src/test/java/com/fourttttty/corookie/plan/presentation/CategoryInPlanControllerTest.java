package com.fourttttty.corookie.plan.presentation;

import com.fourttttty.corookie.member.domain.AuthProvider;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import com.fourttttty.corookie.plan.application.service.CategoryInPlanService;
import com.fourttttty.corookie.plan.application.service.PlanCategoryService;
import com.fourttttty.corookie.plan.domain.PlanCategory;
import com.fourttttty.corookie.plan.dto.request.PlanCategoryCreateRequest;
import com.fourttttty.corookie.plan.dto.response.PlanCategoryResponse;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.support.RestDocsTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.fourttttty.corookie.support.ApiDocumentUtils.getDocumentRequest;
import static com.fourttttty.corookie.support.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryInPlanController.class)
class CategoryInPlanControllerTest extends RestDocsTest {

    @MockBean
    private CategoryInPlanService categoryInPlanService;

    private Member member;
    private Project project;
    private List<PlanCategory> planCategories;

    @BeforeEach
    void initTexture() {
        member = Member.of("memberName", "test@gmail.com", "https://test", Oauth2.of(AuthProvider.KAKAO, "account"));
        project = Project.of("memberName", "description", true,
                "http://test.com", false, member);

        planCategories = List.of(
                PlanCategory.of(1L, "testCategory1", "#ffddaa", project),
                PlanCategory.of(2L, "testCategory2", "#ffddaa", project));
    }

    @Test
    @DisplayName("일정에 있는 카테고리 목록 조회")
    void categoryList() throws Exception {
        List<PlanCategoryResponse> response = List.of(new PlanCategoryResponse(1L, "content", "#ffddaa"));
        given(categoryInPlanService.findAllByPlanId(any(Long.class))).willReturn(response);

        ResultActions perform = mockMvc.perform(
                get("/api/v1/projects/{projectId}/plans/{planId}/categories", 1L, 1L));

        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.get(0).id()))
                .andExpect(jsonPath("$[0].content").value(response.get(0).content()))
                .andExpect(jsonPath("$[0].color").value(response.get(0).color()));

        perform.andDo(print())
                .andDo(document("category-list-in-plan",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("planId").description("일정 키")),
                        responseFields(
                                fieldWithPath("[].id").type(NUMBER).description("카테고리 키"),
                                fieldWithPath("[].content").type(STRING).description("카테고리 내용"),
                                fieldWithPath("[].color").type(STRING).description("카테고리 색"))));
    }

    @Test
    @DisplayName("일정에 카테고리 추가")
    void categoryCreate() throws Exception {
        //given
        PlanCategoryResponse response = PlanCategoryResponse.from(planCategories.get(0));
        given(categoryInPlanService.create(any(Long.class), any(PlanCategoryCreateRequest.class)))
                .willReturn(response);

        PlanCategoryCreateRequest request = new PlanCategoryCreateRequest(
                planCategories.get(0).getContent(), planCategories.get(0).getColor());

        //when
        ResultActions perform = mockMvc.perform(
                post("/api/v1/projects/{projectId}/plans/{planId}/categories", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)));

        //then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.content").value(response.content()))
                .andExpect(jsonPath("$.color").value(response.color()));

        perform.andDo(print())
                .andDo(document("category-create-in-plan",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("planId").description("일정 키")),
                        requestFields(
                                fieldWithPath("content").type(STRING).description("카테고리 내용"),
                                fieldWithPath("color").type(STRING).description("카테고리 색")),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("카테고리 키"),
                                fieldWithPath("content").type(STRING).description("카테고리 내용"),
                                fieldWithPath("color").type(STRING).description("카테고리 색"))));
    }

    @Test
    @DisplayName("일정에 있는 카테고리 삭제")
    void categoryDelete() throws Exception {
        //when
        ResultActions perform = mockMvc.perform(delete(
                "/api/v1/projects/{projectId}/plans/{planId}/categories/{categoryId}",
                1L, 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(status().isNoContent());

        perform.andDo(print())
                .andDo(document("category-delete-in-plan",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("planId").description("일정 키"),
                                parameterWithName("categoryId").description("프로젝트 키"))));
    }
}