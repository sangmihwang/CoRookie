package com.fourttttty.corookie.textchannel.presentation;

import com.fourttttty.corookie.config.security.LoginUser;
import com.fourttttty.corookie.textchannel.application.service.TextChannelService;
import com.fourttttty.corookie.textchannel.dto.request.TextChannelCreateRequest;
import com.fourttttty.corookie.textchannel.dto.request.TextChannelModifyRequest;
import com.fourttttty.corookie.textchannel.dto.response.TextChannelResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects/{projectId}/text-channels")
public class TextChannelController {

    private final TextChannelService textChannelService;

    @GetMapping
    public ResponseEntity<List<TextChannelResponse>> textChannelList(@PathVariable Long projectId,
                                                                     @AuthenticationPrincipal LoginUser loginUser) {
        return ResponseEntity.ok(textChannelService.findByProjectId(projectId, loginUser.getMemberId()));
    }

    @GetMapping("/{textChannelId}")
    public ResponseEntity<TextChannelResponse> textChannelDetail(@PathVariable Long projectId,
                                                                 @PathVariable Long textChannelId,
                                                                 @AuthenticationPrincipal LoginUser loginUser) {
        return ResponseEntity.ok(textChannelService.findById(textChannelId, loginUser.getMemberId()));
    }

    @PostMapping
    public ResponseEntity<TextChannelResponse> textChannelCreate(@PathVariable Long projectId,
                                                                 @RequestBody @Validated TextChannelCreateRequest request) {
        return ResponseEntity.ok(textChannelService.create(request, projectId));
    }

    @PutMapping("/{textChannelId}")
    public ResponseEntity<TextChannelResponse> textChannelModify(@PathVariable Long projectId,
                                                                 @PathVariable Long textChannelId,
                                                                 @RequestBody @Validated TextChannelModifyRequest request,
                                                                 @AuthenticationPrincipal LoginUser loginUser) {
        return ResponseEntity.ok(textChannelService.modify(textChannelId, request, loginUser.getMemberId()));
    }

    @DeleteMapping("/{textChannelId}")
    public ResponseEntity<Object> textChannelDelete(@PathVariable Long projectId,
                                                    @PathVariable Long textChannelId) {
        textChannelService.delete(textChannelId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{textChannelId}/pin")
    public ResponseEntity<Objects> textChannelPin(@PathVariable Long projectId,
                                                  @PathVariable Long textChannelId,
                                                  @AuthenticationPrincipal LoginUser loginUser) {
        textChannelService.pinChannel(projectId, textChannelId, loginUser.getMemberId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{textChannelId}/unpin")
    public ResponseEntity<Objects> textChannelUnpin(@PathVariable Long projectId,
                                                    @PathVariable Long textChannelId,
                                                    @AuthenticationPrincipal LoginUser loginUser) {
        textChannelService.unpinChannel(projectId, textChannelId, loginUser.getMemberId());
        return ResponseEntity.noContent().build();
    }
}
