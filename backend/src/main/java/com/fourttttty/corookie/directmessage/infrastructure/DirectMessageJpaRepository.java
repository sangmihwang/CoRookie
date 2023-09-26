package com.fourttttty.corookie.directmessage.infrastructure;

import com.fourttttty.corookie.directmessage.domain.DirectMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectMessageJpaRepository extends JpaRepository<DirectMessage, Long> {

    Page<DirectMessage> findByDirectMessageChannelId(Long directMessageChannelId, Pageable pageable);
}
