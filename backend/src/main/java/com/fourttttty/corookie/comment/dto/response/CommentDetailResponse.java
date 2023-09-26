package com.fourttttty.corookie.comment.dto.response;

import com.fourttttty.corookie.comment.domain.Comment;
import com.fourttttty.corookie.member.dto.response.MemberResponse;

import java.time.LocalDateTime;

public record CommentDetailResponse(Long id,
                                    String content,
                                    MemberResponse writer,
                                    LocalDateTime createdAt) {

    public static CommentDetailResponse from(Comment comment) {
        return new CommentDetailResponse(
                comment.getId(),
                comment.getContent(),
                MemberResponse.from(comment.getWriter()),
                comment.getCreatedAt());
    }
}
