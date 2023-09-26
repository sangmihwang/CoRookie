package com.fourttttty.corookie.thread.application.repository;

import com.fourttttty.corookie.thread.domain.Emoji;
import com.fourttttty.corookie.thread.domain.ThreadEmoji;

import java.util.List;
import java.util.Optional;

public interface ThreadEmojiRepository {
    ThreadEmoji save(ThreadEmoji threadEmoji);
    List<ThreadEmoji> findByThreadId(Long threadId);
    Optional<ThreadEmoji> findByMemberAndEmojiAndThread(Emoji emoji, Long memberId, Long threadId);
    Long countByEmojiAndThread(Emoji emoji, Long threadId);
    public Boolean existsByMemberAndEmojiAndThread(Emoji emoji, Long memberId, Long threadId);
    void delete(ThreadEmoji threadEmoji);
    void deleteById(Long id);
}
