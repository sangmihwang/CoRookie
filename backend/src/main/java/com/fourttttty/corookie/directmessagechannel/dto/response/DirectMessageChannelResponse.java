package com.fourttttty.corookie.directmessagechannel.dto.response;

import com.fourttttty.corookie.directmessagechannel.domain.DirectMessageChannel;
import lombok.Builder;

@Builder
public record DirectMessageChannelResponse(Long id,
                                           Long member1Id,
                                           String member1Name,
                                           String member1ImageUrl,
                                           Long member2Id,
                                           String member2Name,
                                           String member2ImageUrl) {

    public static DirectMessageChannelResponse of(DirectMessageChannel directMessageChannel) {
        return DirectMessageChannelResponse.builder()
                .id(directMessageChannel.getId())
                .member1Id(directMessageChannel.getMember1().getId())
                .member1Name(directMessageChannel.getMember1().getName())
                .member1ImageUrl(directMessageChannel.getMember1().getImageUrl())
                .member2Id(directMessageChannel.getMember2().getId())
                .member2Name(directMessageChannel.getMember2().getName())
                .member2ImageUrl(directMessageChannel.getMember2().getImageUrl())
                .build();
    }
}
