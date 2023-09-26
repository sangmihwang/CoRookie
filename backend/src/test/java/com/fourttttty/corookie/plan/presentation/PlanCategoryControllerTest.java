package com.fourttttty.corookie.plan.presentation;

import com.fourttttty.corookie.member.domain.AuthProvider;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import com.fourttttty.corookie.plan.application.service.PlanCategoryService;

import static com.fourttttty.corookie.support.ApiDocumentUtils.getDocumentRequest;
import static com.fourttttty.corookie.support.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;

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

import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlanCategoryController.class)
class PlanCategoryControllerTest extends RestDocsTest {

    @MockBean
    private PlanCategoryService planCategoryService;

    private Member member;
    private Project project;

    @BeforeEach
    void initTexture() {
        member = Member.of("memberName", "test@gmail.com", "https://test", Oauth2.of(AuthProvider.KAKAO, "account"));
        project = Project.of("memberName", "description", true,
                "http://test.com", false, member);
    }

    @Test
    @DisplayName("카테고리 생성")
    void createCategory() throws Exception {
        // given
        PlanCategoryResponse response = new PlanCategoryResponse(1L, "content", "#ffddaa");
        given(planCategoryService.create(any(PlanCategoryCreateRequest.class), any(Long.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(post("/api/v1/projects/{projectId}/plan-categories",
                1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new PlanCategoryCreateRequest("content", "#ffddaa"))));

        // then
        perform.andExpect(status().isOk());

        perform.andDo(print())
                .andDo(document("category-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키")),
                        requestFields(
                                fieldWithPath("content").type(STRING).description("카테고리 내용"),
                                fieldWithPath("color").type(STRING).description("카테고리 색")),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("카테고리 키"),
                                fieldWithPath("content").type(STRING).description("카테고리 내용"),
                                fieldWithPath("color").type(STRING).description("카테고리 색"))));
    }

    @Test
    @DisplayName("카테고리 목록 조회")
    void findCategories() throws Exception {
        // given
        PlanCategoryResponse response = new PlanCategoryResponse(1L, "content", "#ffddaa");
        given(planCategoryService.findByProjectId(any(Long.class)))
                .willReturn(List.of(response));

        // when
        ResultActions perform = mockMvc.perform(get("/api/v1/projects/{projectId}/plan-categories",
                1L)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk());

        perform.andDo(print())
                .andDo(document("category-list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키")),
                        responseFields(
                                fieldWithPath("[].id").type(NUMBER).description("카테고리 키"),
                                fieldWithPath("[].content").type(STRING).description("카테고리 내용"),
                                fieldWithPath("[].color").type(STRING).description("카테고리 색"))));
    }

    @Test
    @DisplayName("카테고리 삭제")
    void deleteCategory() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(delete("/api/v1/projects/{projectId}/plan-categories/{categoryId}",
                1L, 1L)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isNoContent());

        perform.andDo(print())
                .andDo(document("category-delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("categoryId").description("카테고리 키"))));
    }
}