package com.fourttttty.corookie.directmessage.application.repository;

import com.fourttttty.corookie.directmessage.domain.DirectMessage;
import com.fourttttty.corookie.directmessage.infrastructure.DirectMessageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DirectMessageRepositoryImpl implements DirectMessageRepository {

    private final DirectMessageJpaRepository directMessageJpaRepository;

    @Override
    public DirectMessage save(DirectMessage directMessage) {
        return directMessageJpaRepository.save(directMessage);
    }

    @Override
    public Optional<DirectMessage> findById(Long directMessageId) {
        return directMessageJpaRepository.findById(directMessageId);
    }

    @Override
    public Page<DirectMessage> findByDirectChannelIdLatest(Long directChannelId, Pageable pageable) {
        return directMessageJpaRepository.findByDirectMessageChannelId(directChannelId, pageable);
    }
}
