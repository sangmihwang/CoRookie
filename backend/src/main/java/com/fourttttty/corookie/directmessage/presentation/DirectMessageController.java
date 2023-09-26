package com.fourttttty.corookie.directmessage.presentation;

import com.fourttttty.corookie.directmessage.application.service.DirectMessageService;
import com.fourttttty.corookie.directmessage.dto.request.DirectMessageCreateRequest;
import com.fourttttty.corookie.directmessage.dto.response.DirectMessageDetailResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/directs/{directChannelId}/messages")
@RequiredArgsConstructor
public class DirectMessageController {

    private final DirectMessageService directMessageService;
    private final SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/message")
    public ResponseEntity<DirectMessageDetailResponse> messageCreate(DirectMessageCreateRequest request) {
        DirectMessageDetailResponse response = directMessageService.create(request);
        System.out.println("res = " + request.directChannelId());
        sendingOperations.convertAndSend("/topic/message/" + request.directChannelId(), response);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{directMessageId}")
    public ResponseEntity<DirectMessageDetailResponse> directMessageDetail(@PathVariable Long directMessageId) {
        return ResponseEntity.ok(directMessageService.findById(directMessageId));
    }

    @GetMapping
    public ResponseEntity<List<DirectMessageDetailResponse>> directMessageList(@PathVariable Long directChannelId,
                                                                               @PageableDefault(
                                                                                       size = 10,
                                                                                       sort = "createdAt",
                                                                                       direction = Sort.Direction.DESC)
                                                                               Pageable pageable) {
        return ResponseEntity.ok(directMessageService.findByDirectChannelIdLatest(directChannelId, pageable));
    }

    @DeleteMapping("/{directMessageId}")
    public ResponseEntity<Object> directMessageDelete(@PathVariable Long directMessageId) {
        directMessageService.delete(directMessageId);
        return ResponseEntity.noContent().build();
    }
}
