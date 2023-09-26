package com.fourttttty.corookie.directmessage.dto.response;

import com.fourttttty.corookie.directmessage.domain.DirectMessage;
import com.fourttttty.corookie.member.dto.response.MemberResponse;

import java.time.LocalDateTime;

public record DirectMessageDetailResponse(Long id,
                                          String content,
                                          LocalDateTime createdAt,
                                          MemberResponse writer) {

    public static DirectMessageDetailResponse from(DirectMessage directMessage) {
        return new DirectMessageDetailResponse(
                directMessage.getId(),
                directMessage.getContent(),
                directMessage.getCreatedAt(),
                MemberResponse.from(directMessage.getWriter()));
    }
}
