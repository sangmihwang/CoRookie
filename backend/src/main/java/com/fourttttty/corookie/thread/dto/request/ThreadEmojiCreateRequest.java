package com.fourttttty.corookie.thread.dto.request;

import com.fourttttty.corookie.thread.domain.Emoji;
import jakarta.validation.constraints.NotNull;

public record ThreadEmojiCreateRequest(@NotNull Emoji emoji) {
}
