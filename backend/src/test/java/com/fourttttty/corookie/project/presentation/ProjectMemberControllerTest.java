package com.fourttttty.corookie.project.presentation;

import com.fourttttty.corookie.member.domain.AuthProvider;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import com.fourttttty.corookie.project.application.service.ProjectMemberService;
import com.fourttttty.corookie.project.dto.request.ProjectMemberCreateRequest;
import com.fourttttty.corookie.project.dto.response.ProjectMemberResponse;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProjectMemberController.class)
public class ProjectMemberControllerTest extends RestDocsTest {

    @MockBean
    private ProjectMemberService projectMemberService;

    private Member member;

    @BeforeEach
    void initTexture() {
        member = Member.of("memberName", "test@gmail.com", "https://test", Oauth2.of(AuthProvider.KAKAO, "account"));
    }

    @Test
    @DisplayName("프로젝트에 회원을 등록한다")
    void projectMemberCreate() throws Exception {
        // given
        Long projectId = 1L;
        Long memberId = 1L;
        ProjectMemberResponse response = new ProjectMemberResponse(
                1L,
                member.getName(),
                member.getEmail(),
                member.getImageUrl(),
                false);
        given(projectMemberService.create(any(ProjectMemberCreateRequest.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(post("/api/v1/projects/{projectId}/projectmembers", projectId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new ProjectMemberCreateRequest(projectId, memberId))));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(response.memberId()))
                .andExpect(jsonPath("$.memberName").value(response.memberName()))
                .andExpect(jsonPath("$.memberEmail").value(response.memberEmail()))
                .andExpect(jsonPath("$.memberImageUrl").value(response.memberImageUrl()))
                .andExpect(jsonPath("$.isManager").value(response.isManager()));

        perform.andDo(print())
                .andDo(document("projectmember-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("projectId").type(NUMBER).description("프로젝트 키"),
                                fieldWithPath("memberId").type(NUMBER).description("회원 키")),
                        responseFields(
                                fieldWithPath("memberId").type(NUMBER).description("멤버 키"),
                                fieldWithPath("memberName").type(STRING).description("멤버 이름"),
                                fieldWithPath("memberEmail").type(STRING).description("멤버 이메일"),
                                fieldWithPath("memberImageUrl").type(STRING).description("멤버 프로필 사진 url"),
                                fieldWithPath("isManager").type(BOOLEAN).description("프로젝트 관리자 여부"))));
    }

    @Test
    @DisplayName("프로젝트-회원 관계를 삭제한다")
    void projectMemberDelete() throws Exception {
        ResultActions perform = mockMvc.perform(
                delete("/api/v1/projects/{projectId}/projectmembers/{memberId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON));

        perform.andExpect(status().isNoContent());

        perform.andDo(print())
                .andDo(document("projectmember-delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("memberId").description("멤버 키"))));
    }

    @Test
    @DisplayName("프로젝트에 등록된 회원 목록을 조회한다")
    void projectMemberList() throws Exception {
        // given
        ProjectMemberResponse response = new ProjectMemberResponse(1L,
                member.getName(),
                member.getEmail(),
                member.getImageUrl(),
                true);
        given(projectMemberService.findByProjectId(any(Long.class))).willReturn(List.of(response));

        // when
        ResultActions perform = mockMvc.perform(get("/api/v1/projects/{projectId}/projectmembers",
                1L));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].memberId").value(response.memberId()))
                .andExpect(jsonPath("$[0].memberName").value(response.memberName()))
                .andExpect(jsonPath("$[0].memberEmail").value(response.memberEmail()))
                .andExpect(jsonPath("$[0].memberImageUrl").value(response.memberImageUrl()))
                .andExpect(jsonPath("$[0].isManager").value(response.isManager()));

        perform.andDo(print())
                .andDo(document("projectmember-list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키")),
                        responseFields(
                                fieldWithPath("[].memberId").type(NUMBER).description("멤버 키"),
                                fieldWithPath("[].memberName").type(STRING).description("멤버 이름"),
                                fieldWithPath("[].memberEmail").type(STRING).description("멤버 이메일"),
                                fieldWithPath("[].memberImageUrl").type(STRING).description("멤버 프로필 사진 url"),
                                fieldWithPath("[].isManager").type(BOOLEAN).description("프로젝트 관리자 여부"))));
    }
}
