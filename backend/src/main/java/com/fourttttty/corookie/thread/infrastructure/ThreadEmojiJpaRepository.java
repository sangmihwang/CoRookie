package com.fourttttty.corookie.thread.infrastructure;

import com.fourttttty.corookie.thread.domain.Emoji;
import com.fourttttty.corookie.thread.domain.ThreadEmoji;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ThreadEmojiJpaRepository extends JpaRepository<ThreadEmoji, Long> {

    List<ThreadEmoji> findByThreadId(Long threadId);
    Optional<ThreadEmoji> findByEmojiAndMemberIdAndThreadId(Emoji emoji, Long memberId, Long threadId);
    Boolean existsByEmojiAndMemberIdAndThreadId(Emoji emoji, Long memberId, Long threadId);
    Long countByEmojiAndThreadId(Emoji emoji, Long threadId);
}
