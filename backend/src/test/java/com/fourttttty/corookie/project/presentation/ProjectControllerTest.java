package com.fourttttty.corookie.project.presentation;

import com.fourttttty.corookie.project.application.service.ProjectService;
import com.fourttttty.corookie.project.dto.request.ProjectUpdateRequest;
import com.fourttttty.corookie.project.dto.request.ProjectCreateRequest;
import com.fourttttty.corookie.project.dto.response.ProjectDetailResponse;
import com.fourttttty.corookie.project.dto.response.ProjectListResponse;
import com.fourttttty.corookie.support.RestDocsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import java.time.LocalDateTime;
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


@WebMvcTest(ProjectController.class)
public class ProjectControllerTest extends RestDocsTest {

    @MockBean
    private ProjectService projectService;

    @Test
    @DisplayName("프로젝트 목록 조회")
    void projectList() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.now();
        ProjectListResponse response = ProjectListResponse.builder()
                .id(1L)
                .name("name")
                .description("description")
                .createdAt(now)
                .updatedAt(now)
                .enabled(true)
                .build();
        given(projectService.findByParticipantId(any(Long.class))).willReturn(List.of(response));

        //when
        ResultActions perform = mockMvc.perform(get("/api/v1/projects"));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()))
                .andExpect(jsonPath("$[0].name").value(response.name()))
                .andExpect(jsonPath("$[0].description").value(response.description()))
                .andExpect(jsonPath("$[0].createdAt").value(toJson(now).replace("\"", "")))
                .andExpect(jsonPath("$[0].updatedAt").value(toJson(now).replace("\"", "")))
                .andExpect(jsonPath("$[0].enabled").value(response.enabled()));

        perform.andDo(print())
                .andDo(document("project-list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("[].id").type(NUMBER).description("프로젝트 키"),
                                fieldWithPath("[].name").type(STRING).description("제목"),
                                fieldWithPath("[].description").type(STRING).description("설명"),
                                fieldWithPath("[].enabled").type(BOOLEAN).description("활성화 여부"),
                                fieldWithPath("[].updatedAt").type(STRING).description("수정 날짜"),
                                fieldWithPath("[].createdAt").type(STRING).description("생성 날짜"))));
    }

    @Test
    @DisplayName("프로젝트 상세 조회")
    void projectDetail() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.now();
        ProjectDetailResponse response = ProjectDetailResponse.builder()
                .id(1L)
                .name("name")
                .description("description")
                .createdAt(now)
                .updatedAt(now)
                .enabled(true)
                .invitationLink("http://test.com")
                .invitationStatus(false)
                .managerName("member")
                .isManager(true)
                .build();
        given(projectService.findById(1L, 1L)).willReturn(response);

        //when
        ResultActions perform = mockMvc.perform(get("/api/v1/projects/{projectId}", 1L));

        //then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()))
                .andExpect(jsonPath("$.description").value(response.description()))
                .andExpect(jsonPath("$.createdAt").value(toJson(now).replace("\"", "")))
                .andExpect(jsonPath("$.updatedAt").value(toJson(now).replace("\"", "")))
                .andExpect(jsonPath("$.enabled").value(response.enabled()))
                .andExpect(jsonPath("$.invitationLink").value(response.invitationLink()))
                .andExpect(jsonPath("$.invitationStatus").value(response.invitationStatus()))
                .andExpect(jsonPath("$.managerName").value(response.managerName()))
                .andExpect(jsonPath("$.isManager").value(response.isManager()));

        perform.andDo(print())
                .andDo(document("project-detail",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키")),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("프로젝트 키"),
                                fieldWithPath("name").type(STRING).description("제목"),
                                fieldWithPath("description").type(STRING).description("내용"),
                                fieldWithPath("enabled").type(BOOLEAN).description("활성화 여부"),
                                fieldWithPath("updatedAt").type(STRING).description("수정 날짜"),
                                fieldWithPath("createdAt").type(STRING).description("생성 날짜"),
                                fieldWithPath("invitationLink").type(STRING).description("초대 링크"),
                                fieldWithPath("invitationStatus").type(BOOLEAN).description("초대 상태"),
                                fieldWithPath("managerName").type(STRING).description("관리자 이름"),
                                fieldWithPath("isManager").type(BOOLEAN).description("관리자 여부"))));
    }


    @Test
    @DisplayName("프로젝트 생성")
    void createProject() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        ProjectDetailResponse response = ProjectDetailResponse.builder()
                .id(1L)
                .name("name")
                .description("description")
                .createdAt(now)
                .updatedAt(now)
                .enabled(true)
                .invitationLink("http://test.com")
                .invitationStatus(false)
                .managerName("member")
                .isManager(true)
                .build();
        given(projectService.create(any(ProjectCreateRequest.class), any(Long.class)))
                .willReturn(response);

        //when
        ResultActions perform = mockMvc.perform(post("/api/v1/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new ProjectCreateRequest("name", "description"))));

        //then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()))
                .andExpect(jsonPath("$.createdAt").value(toJson(now).replace("\"", "")))
                .andExpect(jsonPath("$.updatedAt").value(toJson(now).replace("\"", "")))
                .andExpect(jsonPath("$.description").value(response.description()))
                .andExpect(jsonPath("$.enabled").value(response.enabled()))
                .andExpect(jsonPath("$.invitationLink").value(response.invitationLink()))
                .andExpect(jsonPath("$.invitationStatus").value(false))
                .andExpect(jsonPath("$.managerName").value(response.managerName()))
                .andExpect(jsonPath("$.isManager").value(response.isManager()));

        perform.andDo(print())
                .andDo(document("project-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("name").type(STRING).description("제목"),
                                fieldWithPath("description").type(STRING).description("내용")),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("프로젝트 키"),
                                fieldWithPath("name").type(STRING).description("제목"),
                                fieldWithPath("description").type(STRING).description("내용"),
                                fieldWithPath("enabled").type(BOOLEAN).description("활성화 여부"),
                                fieldWithPath("invitationLink").type(STRING).description("초대 링크"),
                                fieldWithPath("createdAt").type(STRING).description("생성 날짜"),
                                fieldWithPath("updatedAt").type(STRING).description("수정 날짜"),
                                fieldWithPath("invitationStatus").type(BOOLEAN).description("초대 상태"),
                                fieldWithPath("managerName").type(STRING).description("관리자 이름"),
                                fieldWithPath("isManager").type(BOOLEAN).description("관리자 여부"))));
    }

    @Test
    @DisplayName("프로젝트 수정")
    void projectModify() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.now();
        ProjectDetailResponse response = ProjectDetailResponse.builder()
                .id(1L)
                .name("memberName")
                .description("description")
                .createdAt(now)
                .updatedAt(now)
                .enabled(true)
                .invitationLink("http://test.com")
                .invitationStatus(false)
                .managerName("member")
                .isManager(true)
                .build();
        given(projectService.modify(any(ProjectUpdateRequest.class), any(Long.class), any(Long.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(put("/api/v1/projects/{projectId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new ProjectUpdateRequest("memberName",
                        "description",
                        "http://test.com",
                        true))));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()))
                .andExpect(jsonPath("$.createdAt").value(toJson(now).replace("\"", "")))
                .andExpect(jsonPath("$.updatedAt").value(toJson(now).replace("\"", "")))
                .andExpect(jsonPath("$.description").value(response.description()))
                .andExpect(jsonPath("$.enabled").value(response.enabled()))
                .andExpect(jsonPath("$.invitationLink").value(response.invitationLink()))
                .andExpect(jsonPath("$.invitationStatus").value(response.invitationStatus()))
                .andExpect(jsonPath("$.managerName").value(response.managerName()))
                .andExpect(jsonPath("$.isManager").value(response.isManager()));

        perform.andDo(print())
                .andDo(document("project-update",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키")),
                        requestFields(
                                fieldWithPath("name").type(STRING).description("제목"),
                                fieldWithPath("description").type(STRING).description("내용"),
                                fieldWithPath("invitationLink").type(STRING).description("초대 링크"),
                                fieldWithPath("invitationStatus").type(BOOLEAN).description("초대 상태")),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("프로젝트 키"),
                                fieldWithPath("name").type(STRING).description("제목"),
                                fieldWithPath("description").type(STRING).description("내용"),
                                fieldWithPath("enabled").type(BOOLEAN).description("활성화 여부"),
                                fieldWithPath("invitationLink").type(STRING).description("초대 링크"),
                                fieldWithPath("createdAt").type(STRING).description("생성 날짜"),
                                fieldWithPath("updatedAt").type(STRING).description("수정 날짜"),
                                fieldWithPath("invitationStatus").type(BOOLEAN).description("초대 상태"),
                                fieldWithPath("managerName").type(STRING).description("관리자 이름"),
                                fieldWithPath("isManager").type(BOOLEAN).description("관리자 여부"))));
    }

    @Test
    @DisplayName("프로젝트 삭제")
    void projectDelete() throws Exception {
        ResultActions perform = mockMvc.perform(delete("/api/v1/projects/{projectId}", 1L))
                .andExpect(status().isNoContent());

        perform.andDo(print())
                .andDo(document("project-delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"))));
    }
}
