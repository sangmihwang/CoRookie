package com.fourttttty.corookie.thread.application.service;

import com.fourttttty.corookie.member.application.service.MemberService;
import com.fourttttty.corookie.textchannel.application.repository.TextChannelRepository;
import com.fourttttty.corookie.textchannel.application.service.TextChannelService;
import com.fourttttty.corookie.textchannel.domain.TextChannel;
import com.fourttttty.corookie.thread.application.repository.ThreadEmojiRepository;
import com.fourttttty.corookie.thread.application.repository.ThreadRepository;
import com.fourttttty.corookie.thread.domain.Thread;
import com.fourttttty.corookie.thread.domain.ThreadEmoji;
import com.fourttttty.corookie.thread.dto.request.ThreadCreateRequest;
import com.fourttttty.corookie.thread.dto.request.ThreadModifyRequest;
import com.fourttttty.corookie.thread.dto.response.ThreadDetailResponse;
import com.fourttttty.corookie.thread.dto.response.ThreadEmojiResponse;
import com.fourttttty.corookie.thread.dto.response.ThreadListResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ThreadService {

    private final ThreadRepository threadRepository;
    private final TextChannelRepository textChannelRepository;
    private final ThreadEmojiRepository threadEmojiRepository;
    private final ThreadEmojiService threadEmojiService;
    private final MemberService memberService;

    @Transactional
    public ThreadDetailResponse create(ThreadCreateRequest request) {
        Thread thread = Thread.of(request.content(), true, 0,
                textChannelRepository.findById(request.textChannelId()).orElseThrow(EntityNotFoundException::new),
                memberService.findEntityById(request.writerId()));
        threadRepository.save(thread);
        List<ThreadEmojiResponse> list = new ArrayList<>();
        return ThreadDetailResponse.from(thread, list);
    }

    public Thread findEntityById(Long threadId) {
        return threadRepository.findById(threadId).orElseThrow(EntityNotFoundException::new);
    }

    public ThreadDetailResponse findById(Long threadId, Long memberId) {
        Thread thread = threadRepository.findById(threadId).orElseThrow(EntityNotFoundException::new);
        List<ThreadEmojiResponse> emojis = threadEmojiService.findByThreadAndMember(threadId, memberId);
        return ThreadDetailResponse.from(thread, emojis);
    }

    public List<ThreadListResponse> findByTextChannelIdLatest(Long TextChannelId, Pageable pageable) {
        return threadRepository.findByTextChannelIdLatest(TextChannelId, pageable)
                .stream()
                .map(thread -> ThreadListResponse.from(thread, findEmojis(thread)))
                .toList();
    }

    private List<ThreadEmojiResponse> findEmojis(Thread thread) {
        return threadEmojiRepository.findByThreadId(thread.getId()).stream()
                .map(emoji -> ThreadEmojiResponse.from(emoji.getEmoji(), getCount(emoji), isClicked(emoji)))
                .toList();
    }

    private Long getCount(ThreadEmoji emoji) {
        return threadEmojiRepository.countByEmojiAndThread(emoji.getEmoji(), emoji.getThreadId());
    }

    private Boolean isClicked(ThreadEmoji emoji) {
        return threadEmojiRepository.existsByMemberAndEmojiAndThread(emoji.getEmoji(), emoji.getMemberId(), emoji.getThreadId());
    }

    @Transactional
    public void delete(Long threadId) {
        threadRepository.findById(threadId).orElseThrow(EntityNotFoundException::new).delete();
    }

    @Transactional
    public ThreadDetailResponse modify(ThreadModifyRequest request, Long threadId, Long memberId) {
        Thread thread = threadRepository.findById(threadId).orElseThrow(EntityNotFoundException::new);
        thread.modify(request);
        List<ThreadEmojiResponse> emojis = threadEmojiService.findByThreadAndMember(threadId, memberId);
        return ThreadDetailResponse.from(thread, emojis);
    }
}
