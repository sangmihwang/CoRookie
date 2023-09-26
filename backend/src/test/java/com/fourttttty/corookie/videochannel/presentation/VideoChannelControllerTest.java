package com.fourttttty.corookie.videochannel.presentation;

import com.fourttttty.corookie.member.domain.AuthProvider;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.support.RestDocsTest;
import com.fourttttty.corookie.videochannel.application.service.VideoChannelService;
import com.fourttttty.corookie.videochannel.domain.VideoChannel;
import com.fourttttty.corookie.videochannel.dto.request.VideoChannelCreateRequest;
import com.fourttttty.corookie.videochannel.dto.request.VideoChannelModifyRequest;
import com.fourttttty.corookie.videochannel.dto.response.VideoChannelResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.UUID;

import static com.fourttttty.corookie.support.ApiDocumentUtils.getDocumentRequest;
import static com.fourttttty.corookie.support.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VideoChannelController.class)
class VideoChannelControllerTest extends RestDocsTest {

    @MockBean
    private VideoChannelService videoChannelService;

    private VideoChannel videoChannel;

    private Project project;

    @BeforeEach
    void initTexture() {
        Member member = Member.of("name", "email", "https://test", Oauth2.of(AuthProvider.KAKAO, "account"));
        project = Project.of("project",
                "descriptiion",
                true,
                "http://test.com",
                false,
                member);
        videoChannel = VideoChannel.of("name",
                true,
                true,
                project,
                "sessionId");
    }

    @Test
    @DisplayName("비디오 채널 생성")
    void createVideoChannel() throws Exception {
        // given
        VideoChannelResponse response = new VideoChannelResponse(1L, "name", "sessionId");
        given(videoChannelService.create(any(VideoChannelCreateRequest.class), any(Long.class)))
                .willReturn(response);

        VideoChannelCreateRequest request = new VideoChannelCreateRequest("name");

        // when
        ResultActions perform = mockMvc.perform(post("/api/v1/projects/{projectId}/video-channels", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.sessionId").value(response.sessionId()));

        perform.andDo(print())
                .andDo(document("video-channel-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키")),
                        requestFields(
                                fieldWithPath("name").type(STRING).description("채널명")),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("비디오 채널 키"),
                                fieldWithPath("name").type(STRING).description("채널명"),
                                fieldWithPath("sessionId").type(STRING).description("세션 키"))));
    }

    @Test
    @DisplayName("비디오 채널 전체 조회")
    void findAllVideoChannel() throws Exception {
        // given
        VideoChannelResponse response1 = new VideoChannelResponse(1L, "name1", "sessionId1");
        VideoChannelResponse response2 = new VideoChannelResponse(2L, "name2", "sessionId2");
        given(videoChannelService.findByProjectId(any(Long.class)))
                .willReturn(List.of(response1, response2));

        // when
        ResultActions perform = mockMvc.perform(get("/api/v1/projects/{projectId}/video-channels", 1L));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(response1.name()))
                .andExpect(jsonPath("$[1].name").value(response2.name()));

        perform.andDo(print())
                .andDo(document("video-channel-find-all",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키")),
                        responseFields(
                                fieldWithPath("[].id").type(NUMBER).description("비디오 채널 키"),
                                fieldWithPath("[].name").type(STRING).description("채널명"),
                                fieldWithPath("[].sessionId").type(STRING).description("세션 키"))));
    }

    @Test
    @DisplayName("비디오 채널 상세 조회")
    void findVideoChannelById() throws Exception {
        // given
        VideoChannelResponse response = new VideoChannelResponse(1L, "name", "sessionId");
        given(videoChannelService.findById(any(Long.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(get("/api/v1/projects/{projectId}/video-channels/{videoChannelId}", 1L, 1L));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("name").value(response.name()))
                .andExpect(jsonPath("sessionId").value(response.sessionId()));

        perform.andDo(print())
                .andDo(document("video-channel-find-by-id",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("videoChannelId").description("비디오 채널 키")),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("비디오 채널 키"),
                                fieldWithPath("name").type(STRING).description("채널명"),
                                fieldWithPath("sessionId").type(STRING).description("세션 키"))));
    }

    @Test
    @DisplayName("비디오 채널 수정")
    void modifyVideoChannel() throws Exception {
        // given
        VideoChannelResponse response = new VideoChannelResponse(1L, "name", "sessionId");
        given(videoChannelService.modify(any(Long.class), any(VideoChannelModifyRequest.class)))
                .willReturn(response);

        VideoChannelModifyRequest request = new VideoChannelModifyRequest("name");

        // when
        ResultActions perform = mockMvc.perform(put("/api/v1/projects/{projectId}/video-channels/{videoChannelId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("name").value(response.name()))
                .andExpect(jsonPath("sessionId").value(response.sessionId()));

        perform.andDo(print())
                .andDo(document("video-channel-modify",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("videoChannelId").description("비디오 채널 키")),
                        requestFields(
                                fieldWithPath("name").type(STRING).description("채널명")),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("비디오 채널 키"),
                                fieldWithPath("name").type(STRING).description("채널명"),
                                fieldWithPath("sessionId").type(STRING).description("세션 키"))));
    }

    @Test
    @DisplayName("비디오 채널 삭제")
    void deleteVideoChannel() throws Exception {
        // given

        // when
        ResultActions perform = mockMvc.perform(delete("/api/v1/projects/{projectId}/video-channels/{videoChannelId}", 1L, 1L));

        // then
        perform.andExpect(status().isNoContent());

        perform.andDo(print())
                .andDo(document("video-channel-delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("videoChannelId").description("비디오 채널 키"))));
    }

}




















