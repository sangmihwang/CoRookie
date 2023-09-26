package com.fourttttty.corookie.comment.application.repository;

import com.fourttttty.corookie.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    Page<Comment> findByThreadId(Long threadId, Pageable pageable);

    Optional<Comment> findById(Long commentId);

    Comment save(Comment comment);


}
