package com.fourttttty.corookie.directmessagechannel.presentation;

import com.fourttttty.corookie.config.security.LoginUser;
import com.fourttttty.corookie.directmessagechannel.application.service.DirectMessageChannelService;
import com.fourttttty.corookie.directmessagechannel.dto.response.DirectMessageChannelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/directs")
@RequiredArgsConstructor
public class DirectMessageChannelController {

    private final DirectMessageChannelService directMessageChannelService;

    @GetMapping
    public ResponseEntity<List<DirectMessageChannelResponse>> directList(@PathVariable Long projectId,
                                                                         @AuthenticationPrincipal LoginUser loginUser) {
        return ResponseEntity.ok(directMessageChannelService.findByMemberId(loginUser.getMemberId(), projectId));
    }

    @GetMapping("/{directChannelId}")
    public ResponseEntity<DirectMessageChannelResponse> directDetail(@PathVariable Long directChannelId) {
        return ResponseEntity.ok(directMessageChannelService.findById(directChannelId));
    }
}
