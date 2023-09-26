package com.fourttttty.corookie.thread.infrastructure;

import com.fourttttty.corookie.thread.domain.Thread;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThreadJpaRepository extends JpaRepository<Thread, Long> {

    Page<Thread> findByTextChannelId(Long textChannelId, Pageable pageable);
}
