package com.fourttttty.corookie.thread.dto.response;

import com.fourttttty.corookie.member.dto.response.MemberResponse;
import com.fourttttty.corookie.thread.domain.Thread;

import java.time.LocalDateTime;
import java.util.List;

public record ThreadListResponse(Long id,
                                 MemberResponse writer,
                                 LocalDateTime createdAt,
                                 String content,
                                 Integer commentCount,
                                 List<ThreadEmojiResponse> emojis) {

    public static ThreadListResponse from(Thread thread, List<ThreadEmojiResponse> emojis) {
        return new ThreadListResponse(
                thread.getId(),
                MemberResponse.from(thread.getWriter()),
                thread.getCreatedAt(),
                thread.getContent(),
                thread.getCommentCount(),
                emojis);
    }
}
