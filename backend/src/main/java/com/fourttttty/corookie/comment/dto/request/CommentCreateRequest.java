package com.fourttttty.corookie.comment.dto.request;

public record CommentCreateRequest(Long threadId,
                                   Long writerId,
                                   String content) {
}
