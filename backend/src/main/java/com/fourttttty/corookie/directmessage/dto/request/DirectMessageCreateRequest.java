package com.fourttttty.corookie.directmessage.dto.request;

import jakarta.validation.constraints.NotNull;

public record DirectMessageCreateRequest(@NotNull String content,
                                         @NotNull Long directChannelId,
                                         @NotNull Long writerId) {
}
