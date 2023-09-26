package com.fourttttty.corookie.thread.application.repository;

import com.fourttttty.corookie.thread.domain.Emoji;
import com.fourttttty.corookie.thread.domain.ThreadEmoji;
import com.fourttttty.corookie.thread.infrastructure.ThreadEmojiJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ThreadEmojiRepositoryImpl implements ThreadEmojiRepository {
    private final ThreadEmojiJpaRepository threadEmojiJpaRepository;

    @Override
    public ThreadEmoji save(ThreadEmoji threadEmoji) {
        return threadEmojiJpaRepository.save(threadEmoji);
    }

    @Override
    public List<ThreadEmoji> findByThreadId(Long threadId) {
        return threadEmojiJpaRepository.findByThreadId(threadId);
    }

    @Override
    public Optional<ThreadEmoji> findByMemberAndEmojiAndThread(Emoji emoji, Long memberId, Long threadId) {
        return threadEmojiJpaRepository.findByEmojiAndMemberIdAndThreadId(emoji, memberId, threadId);
    }

    @Override
    public Long countByEmojiAndThread(Emoji emoji, Long threadId) {
        return threadEmojiJpaRepository.countByEmojiAndThreadId(emoji, threadId);
    }

    @Override
    public Boolean existsByMemberAndEmojiAndThread(Emoji emoji, Long memberId, Long threadId) {
        return threadEmojiJpaRepository.existsByEmojiAndMemberIdAndThreadId(emoji, memberId, threadId);
    }

    @Override
    public void delete(ThreadEmoji threadEmoji) {
        threadEmojiJpaRepository.delete(threadEmoji);
    }

    @Override
    public void deleteById(Long id) {
        threadEmojiJpaRepository.deleteById(id);
    }
}
