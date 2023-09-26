package com.fourttttty.corookie.comment.infrastructure;

import com.fourttttty.corookie.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByThreadId(Long threadId, Pageable pageable);
}
