package com.fourttttty.corookie.textchannel.presentation;

import com.fourttttty.corookie.member.domain.AuthProvider;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.support.RestDocsTest;
import com.fourttttty.corookie.textchannel.application.service.TextChannelService;
import com.fourttttty.corookie.textchannel.domain.TextChannel;
import com.fourttttty.corookie.textchannel.dto.request.TextChannelCreateRequest;
import com.fourttttty.corookie.textchannel.dto.request.TextChannelModifyRequest;
import com.fourttttty.corookie.textchannel.dto.response.TextChannelResponse;
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
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TextChannelController.class)
class TextChannelControllerTest extends RestDocsTest {

    @MockBean
    private TextChannelService textChannelService;

    private Project project;
    private TextChannel textChannel;

    @BeforeEach
    void initTexture() {
        Member member = Member.of("name", "email", "https://test", Oauth2.of(AuthProvider.KAKAO, "account"));
        project = Project.of("project",
                "description",
                true,
                "http://test.com",
                false,
                member);
        textChannel = TextChannel.of("name",
                true,
                true,
                project);
    }

    @Test
    @DisplayName("텍스트 채널 생성")
    void createTextChannel() throws Exception {
        // given
        TextChannelResponse response = new TextChannelResponse(1L, "name", false);
        given(textChannelService.create(any(TextChannelCreateRequest.class), any(Long.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(post("/api/v1/projects/{projectId}/text-channels", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new TextChannelCreateRequest("name"))));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()));

        perform.andDo(print())
                .andDo(document("text-channel-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키")),
                        requestFields(
                                fieldWithPath("name").type(STRING).description("채널명")),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("텍스트 채널 키"),
                                fieldWithPath("name").type(STRING).description("채널명"),
                                fieldWithPath("isPinned").type(BOOLEAN).description("고정 여부"))));
    }

    @Test
    @DisplayName("프로젝트에 해당하는 텍스트 채널 전체 조회")
    void findAllTextChannel() throws Exception {
        // given
        given(textChannelService.findByProjectId(any(Long.class), any(Long.class)))
                .willReturn(List.of(new TextChannelResponse(1L, "name", false),
                        new TextChannelResponse(2L, "name", false)));

        // when
        ResultActions perform = mockMvc.perform(get("/api/v1/projects/{projectId}/text-channels", 1L));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("name"));

        perform.andDo(print())
                .andDo(document("text-channel-find-all",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키")),
                        responseFields(
                                fieldWithPath("[].id").type(NUMBER).description("텍스트 채널 키"),
                                fieldWithPath("[].name").type(STRING).description("채널명"),
                                fieldWithPath("[].isPinned").type(BOOLEAN).description("고정 여부"))));
    }

    @Test
    @DisplayName("텍스트 채널 상세 조회")
    void findTextChannelById() throws Exception {
        // given
        given(textChannelService.findById(any(Long.class), any(Long.class)))
                .willReturn(new TextChannelResponse(1L, "name", false));

        // when
        ResultActions perform = mockMvc.perform(get("/api/v1/projects/{projectId}/text-channels/{textChannelId}", 1L, 1L));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("name").value("name"));

        perform.andDo(print())
                .andDo(document("text-channel-find-by-id",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("textChannelId").description("텍스트 채널 키")),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("텍스트 채널 키"),
                                fieldWithPath("name").type(STRING).description("채널명"),
                                fieldWithPath("isPinned").type(BOOLEAN).description("고정 여부"))));
    }

    @Test
    @DisplayName("텍스트 채널 수정")
    void modifyTextChannel() throws Exception {
        // given
        given(textChannelService.modify(any(Long.class), any(TextChannelModifyRequest.class), any(Long.class)))
                .willReturn(new TextChannelResponse(1L, "name", false));

        TextChannelModifyRequest request = new TextChannelModifyRequest("name");

        // when
        ResultActions perform = mockMvc.perform(put("/api/v1/projects/{projectId}/text-channels/{textChannelId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("name").value("name"));

        perform.andDo(print())
                .andDo(document("text-channel-modify",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("textChannelId").description("텍스트 채널 키")),
                        requestFields(
                                fieldWithPath("name").type(STRING).description("채널명")),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("텍스트 채널 키"),
                                fieldWithPath("name").type(STRING).description("채널명"),
                                fieldWithPath("isPinned").type(BOOLEAN).description("고정 여부"))));
    }

    @Test
    @DisplayName("텍스트 채널 삭제")
    void deleteTextChannel() throws Exception {
        // given

        // when
        ResultActions perform = mockMvc.perform(delete("/api/v1/projects/{projectId}/text-channels/{textChannelId}", 1L, 1L));

        // then
        perform.andExpect(status().isNoContent());

        perform.andDo(print())
                .andDo(document("text-channel-delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("textChannelId").description("텍스트 채널 키"))));
    }

}




















