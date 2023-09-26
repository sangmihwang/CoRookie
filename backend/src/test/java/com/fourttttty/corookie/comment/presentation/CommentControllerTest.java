package com.fourttttty.corookie.comment.presentation;

import com.fourttttty.corookie.comment.application.service.CommentService;
import com.fourttttty.corookie.comment.domain.Comment;
import com.fourttttty.corookie.comment.dto.request.CommentModifyRequest;
import com.fourttttty.corookie.comment.dto.response.CommentDetailResponse;
import com.fourttttty.corookie.member.domain.AuthProvider;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import com.fourttttty.corookie.member.dto.response.MemberResponse;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.support.RestDocsTest;
import com.fourttttty.corookie.textchannel.domain.TextChannel;
import com.fourttttty.corookie.thread.domain.Thread;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static com.fourttttty.corookie.support.ApiDocumentUtils.getDocumentRequest;
import static com.fourttttty.corookie.support.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest extends RestDocsTest {

    @MockBean
    private CommentService commentService;

    @MockBean
    private SimpMessageSendingOperations sendingOperations;

    private Thread thread;

    @BeforeEach
    void initTexture() {
        Member member = Member.of("name", "email", "https://test", Oauth2.of(AuthProvider.KAKAO, "account"));
        Project project = Project.of("project",
                "description",
                true,
                "http://test.com",
                false,
                member);
        TextChannel textChannel = TextChannel.of("channelName",
                true,
                true,
                project);
        thread = Thread.of("content",
                true,
                0,
                textChannel,
                member);
        Comment comment = Comment.of("content",
                true,
                thread,
                member);
    }

    @Test
    @DisplayName("댓글 전체 조회")
    void findAllComment() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.now();
        CommentDetailResponse commentDetailResponse = new CommentDetailResponse(
                1L,
                thread.getContent(),
                new MemberResponse(1L, "name", "email@mail.com", "https://test"),
                now
                );

        given(commentService.findByThreadId(any(Long.class), any(Pageable.class)))
                .willReturn(List.of(commentDetailResponse));

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/v1/projects/{projectId}/text-channels/{textChannelId}/threads/{threadId}/comments?",
                        1L, 1L, 1L)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "createdAt,desc"));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(commentDetailResponse.content()))
                .andExpect(jsonPath("$[0].writer.name").value(commentDetailResponse.writer().name()));

        perform.andDo(print())
                .andDo(document("comment-find-all",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("textChannelId").description("텍스트 채널 키"),
                                parameterWithName("threadId").description("스레드 키")),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("요청 개수"),
                                parameterWithName("sort").description("정렬 기준")),
                        responseFields(
                                fieldWithPath("[].id").type(NUMBER).description("댓글 키"),
                                fieldWithPath("[].content").type(STRING).description("댓글 내용"),
                                fieldWithPath("[].createdAt").type(STRING).description("생성 일자"),
                                fieldWithPath("[].writer.id").type(NUMBER).description("작성자 키"),
                                fieldWithPath("[].writer.name").type(STRING).description("작성자 이름"),
                                fieldWithPath("[].writer.email").type(STRING).description("작성자 이메일"),
                                fieldWithPath("[].writer.imageUrl").type(STRING).description("작성자 프로필 url"))
                ));
    }

    @Test
    @DisplayName("댓글 수정")
    void modifyTextChannel() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.now();
        CommentDetailResponse commentDetailResponse = new CommentDetailResponse(
                1L,
                thread.getContent(),
                new MemberResponse(1L, "name", "email@mail.com", "https://test"),
                now
        );

        given(commentService.modify(any(CommentModifyRequest.class), any(Long.class)))
                .willReturn(commentDetailResponse);

        // when
        ResultActions perform = mockMvc.perform(put("/api/v1/projects/{projectId}/text-channels/{textChannelId}/threads/{threadId}/comments/{commentId}", 1L, 1L, 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(new CommentModifyRequest("content"))));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(commentDetailResponse.content()))
                .andExpect(jsonPath("$.writer.name").value(commentDetailResponse.writer().name()));

        perform.andDo(print())
                .andDo(document("comment-modify",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("textChannelId").description("텍스트 채널 키"),
                                parameterWithName("threadId").description("스레드 키"),
                                parameterWithName("commentId").description("댓글 키")),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("댓글 키"),
                                fieldWithPath("content").type(STRING).description("댓글 내용"),
                                fieldWithPath("createdAt").type(STRING).description("생성 일자"),
                                fieldWithPath("writer.id").type(NUMBER).description("작성자 키"),
                                fieldWithPath("writer.name").type(STRING).description("작성자 이름"),
                                fieldWithPath("writer.email").type(STRING).description("작성자 이메일"),
                                fieldWithPath("writer.imageUrl").type(STRING).description("작성자 프로필 url"))
                ));
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteComment() throws Exception{
        // when
        ResultActions perform = mockMvc.perform(delete("/api/v1/projects/{projectId}/text-channels/{textChannelId}/threads/{threadId}/comments/{commentId}", 1L, 1L, 1L, 1L));

        // then
        perform.andExpect(status().isNoContent());

        perform.andDo(print())
                .andDo(document("comment-delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 키"),
                                parameterWithName("textChannelId").description("텍스트 채널 키"),
                                parameterWithName("threadId").description("스레드 키"),
                                parameterWithName("commentId").description("댓글 키"))));
    }
}