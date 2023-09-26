package com.fourttttty.corookie.texture.thread.application.repository;

import com.fourttttty.corookie.textchannel.domain.TextChannel;
import com.fourttttty.corookie.thread.application.repository.ThreadRepository;
import com.fourttttty.corookie.thread.domain.Thread;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeThreadRepository implements ThreadRepository {

    private long autoIncrementId = 1L;
    private final Map<Long, Thread> store = new HashMap<>();

    @Override
    public Thread save(Thread thread) {
        store.put(autoIncrementId++, thread);
        return thread;
    }

    @Override
    public Optional<Thread> findById(Long threadId) {
        return Optional.ofNullable(store.get(threadId));
    }

    @Override
    public Page<Thread> findByTextChannelIdLatest(Long textChannelId, Pageable pageable) {
        return (Page<Thread>) store.values().stream()
                .filter(thread -> thread.getTextChannel().getId().equals(textChannelId))
                .limit(pageable.getPageSize() * pageable.getOffset())
                .toList();
    }
}
