package com.fourttttty.corookie.texture.directmessagechannel.application.repository;

import com.fourttttty.corookie.directmessagechannel.application.repository.DirectMessageChannelRepository;
import com.fourttttty.corookie.directmessagechannel.domain.DirectMessageChannel;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeDirectMessageChannelRepository implements DirectMessageChannelRepository {

    private long autoIncrementId = 1L;
    private final Map<Long, DirectMessageChannel> store = new HashMap<>();
    @Override
    public DirectMessageChannel save(DirectMessageChannel directMessageChannel) {
        if (directMessageChannel.getId() == null) {
            setIdInEntity(directMessageChannel);
        }
        store.put(autoIncrementId, directMessageChannel);
        autoIncrementId++;
        return directMessageChannel;
    }

    private void setIdInEntity(DirectMessageChannel directMessageChannel) {
        try {
            Field id = DirectMessageChannel.class.getDeclaredField("id");
            id.setAccessible(true);
            id.set(directMessageChannel, 1L);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<DirectMessageChannel> findById(Long directMessageId) {
        return Optional.ofNullable(store.get(directMessageId));
    }

    @Override
    public Optional<DirectMessageChannel> findByMembersId(Long member1Id, Long member2Id, Long projectId) {
        return store.values().stream()
                .filter(directMessageChannel -> directMessageChannel.getProject().getId().equals(projectId))
                .filter(directMessageChannel -> directMessageChannel.getMember1().getId().equals(member1Id))
                .filter(directMessageChannel -> directMessageChannel.getMember2().getId().equals(member2Id))
                .findAny();
    }

    @Override
    public List<DirectMessageChannel> findByMemberId(Long memberId, Long projectId) {
        return store.values().stream()
                .filter(directMessageChannel -> directMessageChannel.getProject().getId().equals(projectId))
                .filter(directMessageChannel -> directMessageChannel.getMember1().getId().equals(memberId) ||
                        directMessageChannel.getMember2().getId().equals(memberId))
                .toList();
    }
}
