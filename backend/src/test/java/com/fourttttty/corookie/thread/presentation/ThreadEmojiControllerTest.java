package com.fourttttty.corookie.thread.presentation;

import com.fourttttty.corookie.member.domain.AuthProvider;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.support.RestDocsTest;
import com.fourttttty.corookie.textchannel.domain.TextChannel;
import com.fourttttty.corookie.thread.application.service.ThreadEmojiService;
import com.fourttttty.corookie.thread.domain.Emoji;
import com.fourttttty.corookie.thread.domain.Thread;
import com.fourttttty.corookie.thread.dto.request.ThreadEmojiCreateRequest;
import com.fourttttty.corookie.thread.dto.response.ThreadEmojiResponse;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ThreadEmojiController.class)
public class ThreadEmojiControllerTest extends RestDocsTest {

    @MockBean
    private ThreadEmojiService threadEmojiService;
    private Member member;
    private Project project;
    private TextChannel textChannel;
    private Thread thread;

    @BeforeEach
    void initTexture() {
        member = Member.of("name", "email", "https://test.com", Oauth2.of(AuthProvider.KAKAO, "account"));
        project = Project.of("memberName",
                "description",
                Boolean.FALSE,
                "http://test.com",
                Boolean.FALSE,
                member);
        textChannel = textChannel.of("channel name", true, true, project);
        thread = Thread.of("content",
                Boolean.TRUE,
                0,
                textChannel,
                member);
    }

    @Test
    @DisplayName("쓰레드 이모지 정보 조회")
    void threadEmojiList() throws Exception {
        // given
        ThreadEmojiResponse response = new ThreadEmojiResponse(Emoji.BAD, 1L, true);
        given(threadEmojiService.findByThreadAndMember(any(Long.class), any(Long.class))).willReturn(List.of(response));

        // when
        ResultActions perform = mockMvc.perform(get("/api/v1/projects/{projectId}/text-channels/{textChannelId}/threads/{threadId}/emojis", 1L, 1L, 1L));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].emoji").value(response.emoji().getValue()))
                .andExpect(jsonPath("$[0].count").value(response.count()));

        perform.andDo(print())
                .andDo(document("threademoji-list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("textChannelId").description("텍스트 채널 키"),
                                parameterWithName("threadId").description("쓰레드 키")),
                        responseFields(
                                fieldWithPath("[].emoji").type(STRING).description("이모지"),
                                fieldWithPath("[].count").type(NUMBER).description("이모지 갯수"),
                                fieldWithPath("[].isClicked").type(BOOLEAN).description("이모지 클릭 여부"))));
    }

    @Test
    @DisplayName("쓰레드에 이모지 추가")
    void threadEmojiCreate() throws Exception {
        // given
        ThreadEmojiCreateRequest request = new ThreadEmojiCreateRequest(Emoji.BAD);
        ThreadEmojiResponse response = ThreadEmojiResponse.from(Emoji.BAD, 1L, true);
        given(threadEmojiService.create(any(ThreadEmojiCreateRequest.class), any(Long.class), any(Long.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(post("/api/v1/projects/{projectId}/text-channels/{textChannelId}/threads/{threadId}/emojis", 1L, 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.emoji").value(response.emoji().getValue()))
                .andExpect(jsonPath("$.count").value(response.count()));

        perform.andDo(print())
                .andDo(document("threademoji-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("emoji").type(STRING).description("이모지"),
                                fieldWithPath("isClicked").type(BOOLEAN).description("회원의 이모지 클릭 여부"),
                                fieldWithPath("count").type(NUMBER).description("이모지 갯수")
                        )));
    }

    @Test
    @DisplayName("쓰레드에 이모지 제거")
    void threadEmojiDelete() throws Exception {
        // given

        // when
        ResultActions perform = mockMvc.perform(delete("/api/v1/projects/{projectId}/text-channels/{textChannelId}/threads/{threadId}/emojis/{emoji}",
                1L, 1L, 1L, "bad")
                .contentType(MediaType.APPLICATION_JSON));

        perform.andExpect(status().isNoContent());

        // then
        perform.andDo(print())
                .andDo(document("threademoji-delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("emoji").description("이모지"),
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("threadId").description("쓰레드 키"),
                                parameterWithName("textChannelId").description("텍스트 채널 키"))));
    }
}
