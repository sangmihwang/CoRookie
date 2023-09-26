package com.fourttttty.corookie.textchannel.dto.response;

import com.fourttttty.corookie.textchannel.domain.TextChannel;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TextChannelResponse(Long id,
                                  String name,
                                  Boolean isPinned) {

    public static TextChannelResponse from(TextChannel textChannel, Boolean isPinned) {
        return TextChannelResponse.builder()
                .id(textChannel.getId())
                .name(textChannel.getChannelName())
                .isPinned(isPinned)
                .build();
    }
}
