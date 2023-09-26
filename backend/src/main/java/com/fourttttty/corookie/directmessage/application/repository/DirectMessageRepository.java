package com.fourttttty.corookie.directmessage.application.repository;

import com.fourttttty.corookie.directmessage.domain.DirectMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DirectMessageRepository {

    DirectMessage save(DirectMessage directMessage);
    Optional<DirectMessage> findById(Long directMessageId);
    Page<DirectMessage> findByDirectChannelIdLatest(Long directChannelId, Pageable pageable);
}
