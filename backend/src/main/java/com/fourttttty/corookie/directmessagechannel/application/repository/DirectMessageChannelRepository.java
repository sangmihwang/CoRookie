package com.fourttttty.corookie.directmessagechannel.application.repository;

import com.fourttttty.corookie.directmessagechannel.domain.DirectMessageChannel;

import java.util.List;
import java.util.Optional;

public interface DirectMessageChannelRepository {
    DirectMessageChannel save(DirectMessageChannel directMessageChannel);
    Optional<DirectMessageChannel> findById(Long directMessageId);

    Optional<DirectMessageChannel> findByMembersId(Long member1Id, Long member2Id, Long projectId);

    List<DirectMessageChannel> findByMemberId(Long memberId, Long projectId);
}
