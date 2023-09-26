package com.fourttttty.corookie.thread.dto.request;

public record ThreadCreateRequest(Long textChannelId,
                                  Long writerId,
                                  String content) {
}
