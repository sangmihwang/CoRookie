package com.fourttttty.corookie.videochannel.presentation;


import com.fourttttty.corookie.videochannel.application.service.VideoChannelService;
import com.fourttttty.corookie.videochannel.dto.request.VideoChannelCreateRequest;
import com.fourttttty.corookie.videochannel.dto.request.VideoChannelModifyRequest;
import com.fourttttty.corookie.videochannel.dto.response.VideoChannelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects/{projectId}/video-channels")
public class VideoChannelController {

    private final VideoChannelService videoChannelService;

    @GetMapping
    public ResponseEntity<List<VideoChannelResponse>> videoChannelList(@PathVariable Long projectId) {
        return ResponseEntity.ok(videoChannelService.findByProjectId(projectId));
    }

    @GetMapping("/{videoChannelId}")
    public ResponseEntity<VideoChannelResponse> videoChannelDetail(@PathVariable Long projectId,
                                                                 @PathVariable Long videoChannelId) {
        return ResponseEntity.ok(videoChannelService.findById(videoChannelId));
    }

    @PostMapping
    public ResponseEntity<VideoChannelResponse> videoChannelCreate(@PathVariable Long projectId,
                                                                 @RequestBody @Validated VideoChannelCreateRequest request) {
        return ResponseEntity.ok(videoChannelService.create(request, projectId));
    }

    @PutMapping("/{videoChannelId}")
    public ResponseEntity<VideoChannelResponse> videoChannelModify(@PathVariable Long projectId,
                                                                 @PathVariable Long videoChannelId,
                                                                 @RequestBody @Validated VideoChannelModifyRequest request) {
        return ResponseEntity.ok(videoChannelService.modify(videoChannelId, request));
    }

    @DeleteMapping("/{videoChannelId}")
    public ResponseEntity<Object> videoChannelDelete(@PathVariable Long projectId,
                                                    @PathVariable Long videoChannelId) {
        videoChannelService.delete(videoChannelId);
        return ResponseEntity.noContent().build();
    }

}



