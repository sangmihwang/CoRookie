package com.fourttttty.corookie.thread.application.repository;

import com.fourttttty.corookie.thread.domain.Thread;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ThreadRepository {

    Thread save(Thread thread);

    Optional<Thread> findById(Long threadId);

    Page<Thread> findByTextChannelIdLatest(Long textChannelId, Pageable pageable);
}
