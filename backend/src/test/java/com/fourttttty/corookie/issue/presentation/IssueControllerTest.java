package com.fourttttty.corookie.issue.presentation;

import com.fourttttty.corookie.config.web.WebConfig;
import com.fourttttty.corookie.issue.application.service.IssueFilteringService;
import com.fourttttty.corookie.issue.application.service.IssueService;
import com.fourttttty.corookie.issue.domain.*;
import com.fourttttty.corookie.issue.dto.request.IssueCreateRequest;
import com.fourttttty.corookie.issue.dto.response.IssueDetailResponse;
import com.fourttttty.corookie.issue.dto.response.IssueListResponse;
import com.fourttttty.corookie.issue.util.IssueFilterType;
import com.fourttttty.corookie.member.domain.AuthProvider;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.support.RestDocsTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.fourttttty.corookie.support.ApiDocumentUtils.getDocumentRequest;
import static com.fourttttty.corookie.support.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(WebConfig.class)
@WebMvcTest(IssueController.class)
class IssueControllerTest extends RestDocsTest {
    @MockBean
    private IssueService issueService;
    @MockBean
    private IssueFilteringService issueFilteringService;

    private Issue issue;
    private Member member;

    @BeforeEach
    void initTexture() {
        member = Member.of("memberName", "test@gmail.com", "https://test", Oauth2.of(AuthProvider.KAKAO, "account"));
        issue = Issue.of("topic",
                "description",
                IssueProgress.TODO,
                IssuePriority.HIGH,
                IssueCategory.BACKEND,
                true,
                Project.of("project",
                        "description",
                        true,
                        "http://test.com",
                        false,
                        member),
                member);
    }

    @Test
    @DisplayName("이슈를 생성한다")
    void createIssue() throws Exception {
        // given
        IssueDetailResponse response = new IssueDetailResponse(1L,
                "topic",
                "description",
                IssueProgress.TODO,
                IssuePriority.HIGH,
                IssueCategory.BACKEND,
                1L,
                member.getName());
        given(issueService.create(any(IssueCreateRequest.class), any(Long.class), any(Long.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(post("/api/v1/projects/{projectId}/issues", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new IssueCreateRequest("topic",
                        "description",
                        IssueProgress.TODO,
                        IssuePriority.HIGH,
                        IssueCategory.BACKEND))));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.topic").value(response.topic()))
                .andExpect(jsonPath("$.description").value(response.description()))
                .andExpect(jsonPath("$.progress").value(response.progress().getValue()))
                .andExpect(jsonPath("$.priority").value(response.priority().getName()))
                .andExpect(jsonPath("$.category").value(response.category().getValue()))
                .andExpect(jsonPath("$.managerId").value(response.managerId()))
                .andExpect(jsonPath("$.managerName").value(response.managerName()));

        perform.andDo(print())
                .andDo(document("issue-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키")),
                        requestFields(
                                fieldWithPath("topic").type(STRING).description("제목"),
                                fieldWithPath("description").type(STRING).description("설명"),
                                fieldWithPath("progress").type(STRING).description("이슈 진행도"),
                                fieldWithPath("priority").type(STRING).description("이슈 중요도"),
                                fieldWithPath("category").type(STRING).description("이슈 카테고리")),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("이슈 키"),
                                fieldWithPath("topic").type(STRING).description("제목"),
                                fieldWithPath("description").type(STRING).description("설명"),
                                fieldWithPath("progress").type(STRING).description("이슈 진행도"),
                                fieldWithPath("priority").type(STRING).description("이슈 중요도"),
                                fieldWithPath("category").type(STRING).description("이슈 카테고리"),
                                fieldWithPath("managerId").type(NUMBER).description("이슈 담당자 키"),
                                fieldWithPath("managerName").type(STRING).description("이슈 담당자 이름"))));
    }

    @Test
    @DisplayName("이슈 목록을 조회한다")
    void issueList() throws Exception {
        // given
        List<IssueListResponse> responses = List.of(new IssueListResponse(1L,
                "topic",
                IssueProgress.TODO,
                IssuePriority.HIGH,
                IssueCategory.BACKEND,
                "memberName",
                "https://test.com"));
        given(issueService.findByProjectId(any(Long.class)))
                .willReturn(responses);

        // when
        ResultActions perform = mockMvc.perform(get("/api/v1/projects/{projectId}/issues", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(responses.get(0).id()))
                .andExpect(jsonPath("$[0].topic").value(responses.get(0).topic()))
                .andExpect(jsonPath("$[0].progress").value(responses.get(0).progress().getValue()))
                .andExpect(jsonPath("$[0].priority").value(responses.get(0).priority().getName()))
                .andExpect(jsonPath("$[0].category").value(responses.get(0).category().getValue()))
                .andExpect(jsonPath("$[0].memberName").value(responses.get(0).memberName()));

        perform.andDo(print())
                .andDo(document("issue-list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키")),
                        responseFields(
                                fieldWithPath("[].id").type(NUMBER).description("이슈 키"),
                                fieldWithPath("[].topic").type(STRING).description("제목"),
                                fieldWithPath("[].progress").type(STRING).description("이슈 진행도"),
                                fieldWithPath("[].priority").type(STRING).description("이슈 중요도"),
                                fieldWithPath("[].category").type(STRING).description("이슈 카테고리"),
                                fieldWithPath("[].memberName").type(STRING).description("멤버 이름"),
                                fieldWithPath("[].memberImageUrl").type(STRING).description("멤버 프로필 사진"))));
    }

    @Test
    @DisplayName("이슈 상세 정보를 조회한다")
    void issueDetail() throws Exception {
        IssueDetailResponse response = new IssueDetailResponse(1L,
                "topic",
                "description",
                IssueProgress.TODO,
                IssuePriority.HIGH,
                IssueCategory.BACKEND,
                1L,
                member.getName());
        given(issueService.findById(any(Long.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(get("/api/v1/projects/{projectId}/issues/{issueId}",
                1L, 1L)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.topic").value(response.topic()))
                .andExpect(jsonPath("$.description").value(response.description()))
                .andExpect(jsonPath("$.progress").value(response.progress().getValue()))
                .andExpect(jsonPath("$.priority").value(response.priority().getName()))
                .andExpect(jsonPath("$.category").value(response.category().getValue()))
                .andExpect(jsonPath("$.managerId").value(response.managerId()))
                .andExpect(jsonPath("$.managerName").value(response.managerName()));

        perform.andDo(print())
                .andDo(document("issue-detail",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("issueId").description("이슈 키")),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("이슈 키"),
                                fieldWithPath("topic").type(STRING).description("제목"),
                                fieldWithPath("description").type(STRING).description("설명"),
                                fieldWithPath("progress").type(STRING).description("이슈 진행도"),
                                fieldWithPath("priority").type(STRING).description("이슈 중요도"),
                                fieldWithPath("category").type(STRING).description("이슈 카테고리"),
                                fieldWithPath("managerId").type(NUMBER).description("이슈 담당자 키"),
                                fieldWithPath("managerName").type(STRING).description("이슈 담당자 이름"))));
    }

    @Test
    @DisplayName("이슈를 삭제한다")
    void issueDelete() throws Exception {
        ResultActions perform = mockMvc.perform(delete("/api/v1/projects/{projectId}/issues/{issueId}",
                1L, 1L)
                .contentType(MediaType.APPLICATION_JSON));

        perform.andExpect(status().isNoContent());

        perform.andDo(print())
                .andDo(document("issue-delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("issueId").description("이슈 키"))));
    }

    @Test
    @DisplayName("특정 조건으로 필터링하여 이슈들를 조회한다")
    void issueListByFiltering() throws Exception {
        // given
        List<IssueListResponse> responses = List.of(new IssueListResponse(1L,
                "topic",
                IssueProgress.TODO,
                IssuePriority.HIGH,
                IssueCategory.BACKEND,
                "memberName",
                "https://test.com"));
        given(issueFilteringService.findByFiltering(any(Long.class), any(IssueFilterType.class), any(String.class)))
                .willReturn(responses);

        // when
        ResultActions perform = mockMvc.perform(get("/api/v1/projects/{projectId}/issues/filter", 1L)
                .queryParam("type", "manager")
                .queryParam("condition", "1")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(responses.get(0).id()))
                .andExpect(jsonPath("$[0].topic").value(responses.get(0).topic()))
                .andExpect(jsonPath("$[0].progress").value(responses.get(0).progress().getValue()))
                .andExpect(jsonPath("$[0].priority").value(responses.get(0).priority().getName()))
                .andExpect(jsonPath("$[0].category").value(responses.get(0).category().getValue()))
                .andExpect(jsonPath("$[0].memberName").value(member.getName()));

        perform.andDo(print())
                .andDo(document("issue-list-filter",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키")),
                        queryParameters(
                                parameterWithName("type").description("필터 타입"),
                                parameterWithName("condition").description("필터 조건")),
                        responseFields(
                                fieldWithPath("[].id").type(NUMBER).description("이슈 키"),
                                fieldWithPath("[].topic").type(STRING).description("제목"),
                                fieldWithPath("[].progress").type(STRING).description("이슈 진행도"),
                                fieldWithPath("[].priority").type(STRING).description("이슈 중요도"),
                                fieldWithPath("[].category").type(STRING).description("이슈 카테고리"),
                                fieldWithPath("[].memberName").type(STRING).description("멤버 이름"),
                                fieldWithPath("[].memberImageUrl").type(STRING).description("멤버 프로필 사진"))));
    }
}