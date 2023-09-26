package com.fourttttty.corookie.thread.presentation;

import com.fourttttty.corookie.config.security.LoginUser;
import com.fourttttty.corookie.thread.application.service.ThreadService;
import com.fourttttty.corookie.thread.dto.request.ThreadCreateRequest;
import com.fourttttty.corookie.thread.dto.request.ThreadModifyRequest;
import com.fourttttty.corookie.thread.dto.response.ThreadDetailResponse;
import com.fourttttty.corookie.thread.dto.response.ThreadListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/projects/{projectId}/text-channels/{textChannelId}/threads")
public class ThreadController {

    private final ThreadService threadService;

    // 특정 브로커로 메시지 전달
    private final SimpMessageSendingOperations sendingOperations;

    // Client가 SEND할 수 있는 경로
    // stompConfig에서 설정한 apllicationDestinationPrefixes와
    // @MessageMapping 경로가 병합됨
    @MessageMapping("/thread")
    public ResponseEntity<ThreadDetailResponse> threadCreate(ThreadCreateRequest request) {
        // 스레드 전달
        ThreadDetailResponse response = threadService.create(request);
        sendingOperations.convertAndSend("/topic/thread/" + request.textChannelId(), response);
        // 스레드 생성
        return ResponseEntity.ok(response);
    }

    // 스레드 상세 보기
    @GetMapping("/{threadId}")
    public ResponseEntity<ThreadDetailResponse> threadDetail(@PathVariable Long projectId,
                                                         @PathVariable Long textChannelId,
                                                         @PathVariable Long threadId,
                                                             @AuthenticationPrincipal LoginUser loginUser) {
        return ResponseEntity.ok(threadService.findById(threadId, loginUser.getMemberId()));
    }

    // 특정 텍스트 채널의 스레드 전체 보기
    @GetMapping
    public ResponseEntity<List<ThreadListResponse>> threadList(@PathVariable Long projectId,
                                                                 @PathVariable Long textChannelId,
                                                                 @PageableDefault(size = 10,
                                                                         sort = "createdAt",
                                                                         direction = Sort.Direction.DESC)
                                                                     Pageable pageable) {
        return ResponseEntity.ok(threadService.findByTextChannelIdLatest(textChannelId, pageable));
    }

    // 스레드 삭제
    @DeleteMapping("/{threadId}")
    public ResponseEntity<Void> threadDelete(@PathVariable Long projectId,
                                             @PathVariable Long textChannelId,
                                             @PathVariable Long threadId) {
        threadService.delete(threadId);
        return ResponseEntity.noContent().build();
    }

    // 스레드 수정
    @PutMapping("/{threadId}")
    public ResponseEntity<ThreadDetailResponse> threadModify(@PathVariable Long projectId,
                                                             @PathVariable Long textChannelId,
                                                             @PathVariable Long threadId,
                                                             @RequestBody ThreadModifyRequest request,
                                                             @AuthenticationPrincipal LoginUser loginUser) {
        return ResponseEntity.ok(threadService.modify(request, threadId, loginUser.getMemberId()));
    }
}
