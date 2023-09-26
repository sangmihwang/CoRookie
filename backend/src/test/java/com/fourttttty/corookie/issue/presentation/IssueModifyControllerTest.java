package com.fourttttty.corookie.issue.presentation;

import com.fourttttty.corookie.config.web.WebConfig;
import com.fourttttty.corookie.issue.application.service.IssueUpdateService;
import com.fourttttty.corookie.issue.domain.Issue;
import com.fourttttty.corookie.issue.domain.IssueCategory;
import com.fourttttty.corookie.issue.domain.IssuePriority;
import com.fourttttty.corookie.issue.domain.IssueProgress;
import com.fourttttty.corookie.issue.dto.request.*;
import com.fourttttty.corookie.issue.dto.response.IssueDetailResponse;
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

import static com.fourttttty.corookie.support.ApiDocumentUtils.getDocumentRequest;
import static com.fourttttty.corookie.support.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(WebConfig.class)
@WebMvcTest(IssueModifyController.class)
class IssueModifyControllerTest extends RestDocsTest {

    @MockBean
    private IssueUpdateService issueUpdateService;

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
    @DisplayName("이슈 진행도를 수정한다")
    void issueProgressModify() throws Exception {
        IssueDetailResponse response = new IssueDetailResponse(1L,
                "topic",
                "description",
                IssueProgress.TODO,
                IssuePriority.HIGH,
                IssueCategory.BACKEND,
                1L,
                member.getName());
        given(issueUpdateService.changeIssueProgress(any(Long.class), any(IssueProgressUpdateRequest.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(put("/api/v1/projects/{projectId}/issues/{issueId}/progress",
                1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new IssueProgressUpdateRequest(IssueProgress.TODO))));

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
                .andDo(document("issue-progress-modify",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("issueId").description("이슈 키")),
                        requestFields(
                                fieldWithPath("progress").type(STRING).description("이슈 진행도")),
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
    @DisplayName("이슈 토픽을 수정한다")
    void issueTopicModify() throws Exception {
        IssueDetailResponse response = new IssueDetailResponse(1L,
                "topic",
                "description",
                IssueProgress.TODO,
                IssuePriority.HIGH,
                IssueCategory.BACKEND,
                1L,
                member.getName());
        given(issueUpdateService.changeIssueTopic(any(Long.class), any(IssueTopicUpdateRequest.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(put("/api/v1/projects/{projectId}/issues/{issueId}/topic",
                1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new IssueTopicUpdateRequest("topic"))));

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
                .andDo(document("issue-topic-modify",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("issueId").description("이슈 키")),
                        requestFields(
                                fieldWithPath("topic").type(STRING).description("이슈 제목")),
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
    @DisplayName("이슈 담당자를 수정한다")
    void issueManagerModify() throws Exception {
        IssueDetailResponse response = new IssueDetailResponse(1L,
                "topic",
                "description",
                IssueProgress.TODO,
                IssuePriority.HIGH,
                IssueCategory.BACKEND,
                1L,
                member.getName());
        given(issueUpdateService.changeIssueManager(any(Long.class), any(IssueManagerUpdateRequest.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(put("/api/v1/projects/{projectId}/issues/{issueId}/manager",
                1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new IssueManagerUpdateRequest(1L))));

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
                .andDo(document("issue-manager-modify",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("issueId").description("이슈 키")),
                        requestFields(
                                fieldWithPath("managerId").type(NUMBER).description("이슈 담당자")),
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
    @DisplayName("이슈 중요도를 수정한다")
    void issuePriorityModify() throws Exception {
        IssueDetailResponse response = new IssueDetailResponse(1L,
                "topic",
                "description",
                IssueProgress.TODO,
                IssuePriority.HIGH,
                IssueCategory.BACKEND,
                1L,
                member.getName());
        given(issueUpdateService.changeIssuePriority(any(Long.class), any(IssuePriorityUpdateRequest.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(put("/api/v1/projects/{projectId}/issues/{issueId}/priority",
                1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new IssuePriorityUpdateRequest(IssuePriority.HIGH))));

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
                .andDo(document("issue-priority-modify",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("issueId").description("이슈 키")),
                        requestFields(
                                fieldWithPath("priority").type(STRING).description("이슈 중요도")),
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
    @DisplayName("이슈 카테고리를 수정한다")
    void issueCategoryModify() throws Exception {
        IssueDetailResponse response = new IssueDetailResponse(1L,
                "topic",
                "description",
                IssueProgress.TODO,
                IssuePriority.HIGH,
                IssueCategory.BACKEND,1L,
                member.getName());
        given(issueUpdateService.changeIssueCategory(any(Long.class), any(IssueCategoryUpdateRequest.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(put("/api/v1/projects/{projectId}/issues/{issueId}/category",
                1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new IssueCategoryUpdateRequest(IssueCategory.BACKEND))));

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
                .andDo(document("issue-category-modify",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("issueId").description("이슈 키")),
                        requestFields(
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
    @DisplayName("이슈 설명을 수정한다")
    void issueDescriptionModify() throws Exception {
        IssueDetailResponse response = new IssueDetailResponse(1L,
                "topic",
                "description",
                IssueProgress.TODO,
                IssuePriority.HIGH,
                IssueCategory.BACKEND,
                1L,
                member.getName());
        given(issueUpdateService.changeIssueDescription(any(Long.class), any(IssueDescriptionUpdateRequest.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(put("/api/v1/projects/{projectId}/issues/{issueId}/description",
                1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new IssueDescriptionUpdateRequest("description"))));

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
                .andDo(document("issue-description-modify",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("issueId").description("이슈 키")),
                        requestFields(
                                fieldWithPath("description").type(STRING).description("이슈 설명")),
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
}