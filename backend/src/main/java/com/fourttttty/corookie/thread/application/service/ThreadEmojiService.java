package com.fourttttty.corookie.thread.application.service;

import com.fourttttty.corookie.thread.application.repository.ThreadEmojiRepository;
import com.fourttttty.corookie.thread.domain.Emoji;
import com.fourttttty.corookie.thread.domain.ThreadEmoji;
import com.fourttttty.corookie.thread.dto.request.ThreadEmojiCreateRequest;
import com.fourttttty.corookie.thread.dto.response.ThreadEmojiResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThreadEmojiService {
    private final ThreadEmojiRepository threadEmojiRepository;

    @Transactional
    public ThreadEmojiResponse create(ThreadEmojiCreateRequest request, Long memberId, Long threadId) {
        ThreadEmoji threadEmoji  = threadEmojiRepository.save(ThreadEmoji.of(request.emoji(), memberId, threadId));
        return ThreadEmojiResponse.from(threadEmoji.getEmoji(),
                threadEmojiRepository.countByEmojiAndThread(request.emoji(), threadId),
                true);
    }

    @Transactional
    public void delete(Emoji emoji, Long memberId, Long threadId) {
        threadEmojiRepository.delete(threadEmojiRepository.findByMemberAndEmojiAndThread(emoji, memberId, threadId)
                .orElseThrow(EntityNotFoundException::new));
    }

    public List<ThreadEmojiResponse> findByThreadAndMember(Long threadId, Long memberId) {
        return Arrays.stream(Emoji.values())
                .map(emoji -> ThreadEmojiResponse.from(
                        emoji,
                        threadEmojiRepository.countByEmojiAndThread(emoji, threadId),
                        threadEmojiRepository.existsByMemberAndEmojiAndThread(emoji, memberId, threadId)))
                .toList();
    }
}
