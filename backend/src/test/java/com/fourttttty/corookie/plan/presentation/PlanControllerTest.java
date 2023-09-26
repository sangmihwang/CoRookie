package com.fourttttty.corookie.plan.presentation;

import static com.fourttttty.corookie.support.ApiDocumentUtils.getDocumentRequest;
import static com.fourttttty.corookie.support.ApiDocumentUtils.getDocumentResponse;

import com.fourttttty.corookie.member.domain.AuthProvider;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import com.fourttttty.corookie.plan.application.service.PlanService;
import com.fourttttty.corookie.plan.domain.Plan;
import com.fourttttty.corookie.plan.domain.PlanCategory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fourttttty.corookie.plan.domain.PlanMember;
import com.fourttttty.corookie.plan.dto.request.PlanCategoryCreateRequest;
import com.fourttttty.corookie.plan.dto.request.PlanCreateRequest;
import com.fourttttty.corookie.plan.dto.request.PlanMemberCreateRequest;
import com.fourttttty.corookie.plan.dto.request.PlanMemberDeleteRequest;
import com.fourttttty.corookie.plan.dto.request.PlanUpdateRequest;
import com.fourttttty.corookie.plan.dto.response.CalendarPlanResponse;
import com.fourttttty.corookie.plan.dto.response.PlanCategoryResponse;
import com.fourttttty.corookie.plan.dto.response.PlanMemberResponse;
import com.fourttttty.corookie.plan.dto.response.PlanResponse;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.support.RestDocsTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(PlanController.class)
class PlanControllerTest extends RestDocsTest {

    @MockBean
    private PlanService planService;

    private Member member;
    private Project project;
    private Plan plan;
    private List<PlanCategory> planCategories;
    private List<Member> planMembers;

    @BeforeEach
    void initTexture() {
        member = Member.of("memberName", "test@gmail.com", "https://test", Oauth2.of(AuthProvider.KAKAO, "account"));
        project = Project.of("memberName", "description", true,
                "http://test.com", false, member);

        plan = Plan.of(1L,
                "memberName",
                "testDescription",
                LocalDateTime.now(),
                LocalDateTime.now().minusDays(2),
                true,
                project);

        planCategories = List.of(
                PlanCategory.of(1L, "testCategory1", "#ffddaa", project),
                PlanCategory.of(2L, "testCategory2", "#ffddaa", project));

        planMembers = List.of(
                Member.of(1L, "name1", "test@gmail.com", "https://test",
                        Oauth2.of(AuthProvider.KAKAO, "account")),
                Member.of(2L, "name2", "test@gmail.com", "https://test",
                        Oauth2.of(AuthProvider.KAKAO, "account")),
                Member.of(4L, "name3", "test@gmail.com", "https://test",
                        Oauth2.of(AuthProvider.KAKAO, "account")));
    }

    @Test
    @DisplayName("년 월로 일정을 조회한다")
    void planCalendarList() throws Exception {
        CalendarPlanResponse response = CalendarPlanResponse.from(plan, "#ffddaa");
        given(planService.findByProjectIdAndDate(any(Long.class), any(LocalDate.class))).willReturn(List.of(response));
        //when
        ResultActions perform = mockMvc.perform(get("/api/v1/projects/{projectId}/plans", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("date", String.valueOf(LocalDate.of(2023, 7, 2))));

        //then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].planName").value(response.planName()))
                .andExpect(jsonPath("$[0].planStart").value(toJson(response.planStart()).replace("\"", "")))
                .andExpect(jsonPath("$[0].planEnd").value(toJson(response.planEnd()).replace("\"", "")));

        perform.andDo(print())
                .andDo(document("plan-list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키")),
                        queryParameters(
                                parameterWithName("date").description("캘린더 월")),
                        responseFields(
                                fieldWithPath("[].id").type(NUMBER).description("일정 키"),
                                fieldWithPath("[].planName").type(STRING).description("일정 이름"),
                                fieldWithPath("[].planStart").type(STRING).description("일정 시작일"),
                                fieldWithPath("[].planEnd").type(STRING).description("일정 종료일"),
                                fieldWithPath("[].color").type(STRING).description("일정 색"))));
    }

    @Test
    @DisplayName("일정 생성")
    void planCreate() throws Exception {
        //given
        PlanResponse response = PlanResponse.from(plan,
                planCategories.stream()
                        .map(PlanCategoryResponse::from)
                        .toList(),
                planMembers.stream()
                        .map(member -> PlanMemberResponse.from(PlanMember.of(member, plan)))
                        .toList());
        given(planService.createPlan(any(PlanCreateRequest.class), any(Long.class))).willReturn(response);

        //when
        ResultActions perform = mockMvc.perform(post("/api/v1/projects/{projectId}/plans", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new PlanCreateRequest(
                        plan.getPlanName(),
                        plan.getDescription(),
                        plan.getPlanStart(),
                        plan.getPlanEnd(),
                        List.of(1L),
                        List.of(1L)))));

        //then
        perform.andExpect(status().isOk())
            .andExpect(jsonPath("$.planName").value(response.planName()))
            .andExpect(jsonPath("$.description").value(response.description()))
            .andExpect(
                jsonPath("$.categories[0].content").value(response.categories().get(0).content()))
            .andExpect(jsonPath("$.members[0].memberId").value(response.members().get(0).memberId()));

        perform.andDo(print())
                .andDo(document("plan-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키")),
                        requestFields(
                                fieldWithPath("planName").type(STRING).description("일정 이름"),
                                fieldWithPath("description").type(STRING).description("일정 설명"),
                                fieldWithPath("planStart").type(STRING).description("일정 시작일"),
                                fieldWithPath("planEnd").type(STRING).description("일정 종료일"),
                                fieldWithPath("categoryIds[]").type(ARRAY).description("일정 카테고리 키"),
                                fieldWithPath("memberIds[]").type(ARRAY).description("일정 멤버 키")),
                        responseFields(
                                fieldWithPath("planName").type(STRING).description("일정 이름"),
                                fieldWithPath("description").type(STRING).description("일정 설명"),
                                fieldWithPath("planStart").type(STRING).description("일정 시작일"),
                                fieldWithPath("planEnd").type(STRING).description("일정 종료일"),
                                fieldWithPath("enabled").type(BOOLEAN).description("활성화"),
                                fieldWithPath("categories.[].id").type(NUMBER).description("일정 카테고리 키"),
                                fieldWithPath("categories.[].content").type(STRING).description("일정 카테고리 내용"),
                                fieldWithPath("categories.[].color").type(STRING).description("일정 카테고리 색"),
                                fieldWithPath("members.[].memberId").type(NUMBER).description("일정 멤버 키"),
                                fieldWithPath("members.[].memberName").type(STRING).description("일정 멤버 내용"),
                                fieldWithPath("planId").type(NUMBER).description("일정 키"))));
    }

    @Test
    @DisplayName("일정을 상세 조회 한다")
    void planDetail() throws Exception {
        //given
        PlanResponse planResponse = PlanResponse.from(plan,
                planCategories.stream()
                        .map(PlanCategoryResponse::from)
                        .toList(),
                planMembers.stream()
                        .map(member -> PlanMemberResponse.from(PlanMember.of(member, plan)))
                        .toList());

        given(planService.findById(any(Long.class))).willReturn(planResponse);

        //when
        ResultActions perform = mockMvc.perform(
                get("/api/v1/projects/{projectId}/plans/{planId}", 1L, 1L));

        //then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.planId").value(planResponse.planId()))
                .andExpect(jsonPath("$.planName").value(planResponse.planName()))
                .andExpect(jsonPath("$.description").value(planResponse.description()))
                .andExpect(jsonPath("$.categories[0].content").value(
                        planResponse.categories().get(0).content()))
                .andExpect(jsonPath("$.members[0].memberId").value(
                        planResponse.members().get(0).memberId()))
                .andExpect(jsonPath("$.members[0].memberName").value(
                        planResponse.members().get(0).memberName()));

        perform.andDo(print())
                .andDo(document("plan-detail",
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("planId").description("플랜 memberId")),
                        responseFields(
                                fieldWithPath("planName").type(STRING).description("일정 이름"),
                                fieldWithPath("description").type(STRING).description("일정 설명"),
                                fieldWithPath("planStart").type(STRING).description("일정 시작일"),
                                fieldWithPath("planEnd").type(STRING).description("일정 종료일"),
                                fieldWithPath("enabled").type(BOOLEAN).description("활성화"),
                                fieldWithPath("categories").type(ARRAY).description("일정 카테고리"),
                                fieldWithPath("categories.[].id").type(NUMBER).description("일정 카테고리 키"),
                                fieldWithPath("categories.[].content").type(STRING).description("일정 카테고리 내용"),
                                fieldWithPath("categories.[].color").type(STRING).description("일정 카테고리 색"),
                                fieldWithPath("members").type(ARRAY).description("일정 카테고리"),
                                fieldWithPath("members.[].memberId").type(NUMBER).description("일정 멤버 키"),
                                fieldWithPath("members.[].memberName").type(STRING).description("일정 멤버 내용"),
                                fieldWithPath("planId").type(NUMBER).description("일정 memberId"))));
    }

    @Test
    @DisplayName("일정 수정")
    void planModify() throws Exception {
        //given
        PlanUpdateRequest request = new PlanUpdateRequest(
                plan.getPlanName(),
                plan.getDescription(),
                plan.getPlanStart().minusDays(2),
                plan.getPlanEnd().minusDays(2));

        PlanResponse planResponse = PlanResponse.from(Plan.of(1L,
                        request.planName(),
                        request.description(),
                        request.planStart(),
                        request.planEnd(),
                        true,
                        project),
                planCategories.stream()
                        .map(PlanCategoryResponse::from)
                        .toList(),
                planMembers.stream()
                        .map(member -> PlanMemberResponse.from(PlanMember.of(member, plan)))
                        .toList());

        given(planService.modifyPlan(any(PlanUpdateRequest.class), any(Long.class)))
                .willReturn(planResponse);

        //when
        ResultActions perform = mockMvc.perform(
                put("/api/v1/projects/{projectId}/plans/{planId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)));

        //then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.planId").value(planResponse.planId()))
                .andExpect(jsonPath("$.planName").value(planResponse.planName()))
                .andExpect(jsonPath("$.description").value(planResponse.description()))
                .andExpect(jsonPath("$.categories[0].content").value(
                        planResponse.categories().get(0).content()))
                .andExpect(jsonPath("$.members[0].memberId").value(
                        planResponse.members().get(0).memberId()))
                .andExpect(jsonPath("$.members[0].memberName").value(
                        planResponse.members().get(0).memberName()));

        perform.andDo(print())
                .andDo(document("plan-update",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("planId").description("일정 키")),
                        requestFields(
                                fieldWithPath("planName").type(STRING).description("일정 이름"),
                                fieldWithPath("description").type(STRING).description("일정 설명"),
                                fieldWithPath("planStart").type(STRING).description("일정 시작일"),
                                fieldWithPath("planEnd").type(STRING).description("일정 종료일")),
                        responseFields(
                                fieldWithPath("planName").type(STRING).description("일정 이름"),
                                fieldWithPath("description").type(STRING).description("일정 설명"),
                                fieldWithPath("planStart").type(STRING).description("일정 시작일"),
                                fieldWithPath("planEnd").type(STRING).description("일정 종료일"),
                                fieldWithPath("enabled").type(BOOLEAN).description("활성화"),
                                fieldWithPath("categories").type(ARRAY).description("일정 카테고리"),
                                fieldWithPath("categories.[].id").type(NUMBER).description("일정 카테고리 키"),
                                fieldWithPath("categories.[].content").type(STRING).description("일정 카테고리 내용"),
                                fieldWithPath("categories.[].color").type(STRING).description("일정 카테고리 색"),
                                fieldWithPath("members").type(ARRAY).description("일정 카테고리"),
                                fieldWithPath("members.[].memberId").type(NUMBER).description("일정 멤버 키"),
                                fieldWithPath("members.[].memberName").type(STRING).description("일정 멤버 내용"),
                                fieldWithPath("planId").type(NUMBER).description("일정 키"))));
    }

    @Test
    @DisplayName("일정 삭제")
    void planDelete() throws Exception {
        //given

        //when
        ResultActions perform = mockMvc.perform(
            delete("/api/v1/projects/{projectId}/plans/{planId}", 1L, 1L));

        //then
        perform.andExpect(status().isNoContent());

        perform.andDo(print())
                .andDo(document("plan-delete",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("planId").description("일정 키"))));

    }

    @Test
    @DisplayName("일정 멤버 추가")
    void planMemberCreate() throws Exception {
        //given
        PlanMemberResponse response = PlanMemberResponse.from(PlanMember.of(planMembers.get(0), plan));
        given(planService.createPlanMember(any(Long.class), any(PlanMemberCreateRequest.class)))
                .willReturn(response);

        PlanMemberCreateRequest request = new PlanMemberCreateRequest(planMembers.get(0).getId());

        //when
        ResultActions perform = mockMvc.perform(
                post("/api/v1/projects/{projectId}/plans/{planId}/members", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)));

        //then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(planMembers.get(0).getId()))
                .andExpect(jsonPath("$.memberName").value(planMembers.get(0).getName()));

        perform.andDo(print())
                .andDo(document("planMember-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("planId").description("일정 키")),
                        requestFields(
                                fieldWithPath("memberId").type(NUMBER).description("멤버 키")),
                        responseFields(
                                fieldWithPath("memberId").type(NUMBER).description("멤버 키"),
                                fieldWithPath("memberName").type(STRING).description("멤버 이름"))));
    }

    @Test
    @DisplayName("일정 멤버 삭제")
    void planMemberDelete() throws Exception {
        //given
        PlanMemberDeleteRequest request = new PlanMemberDeleteRequest(planMembers.get(0).getId());

        //when
        ResultActions perform = mockMvc.perform(delete(
                "/api/v1/projects/{projectId}/plans/{planId}/members",
                1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        //then
        perform.andExpect(status().isNoContent());

        perform.andDo(print())
                .andDo(document("planMember-delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("planId").description("일정 키")),
                        requestFields(
                                fieldWithPath("memberId").type(NUMBER).description("멤버 키"))));
    }
}