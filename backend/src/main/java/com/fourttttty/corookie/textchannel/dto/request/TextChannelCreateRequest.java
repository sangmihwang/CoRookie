package com.fourttttty.corookie.textchannel.dto.request;

import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.textchannel.domain.TextChannel;
import jakarta.validation.constraints.NotBlank;

public record TextChannelCreateRequest(@NotBlank String name) {

    public TextChannel toEntity(Project project) {
        return TextChannel.of(name, true, true,  project);
    }
}
