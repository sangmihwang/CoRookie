package com.fourttttty.corookie.videochannel.dto.request;

import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.videochannel.domain.VideoChannel;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record VideoChannelCreateRequest(@NotBlank String name) {

    public VideoChannel toEntity(Project project) {
        return VideoChannel.of(name, true, true, project, UUID.randomUUID().toString());
    }
}
