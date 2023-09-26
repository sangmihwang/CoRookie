package com.fourttttty.corookie.thread.dto.response;

import com.fourttttty.corookie.thread.domain.Emoji;
import com.fourttttty.corookie.thread.domain.ThreadEmoji;

public record ThreadEmojiResponse(Emoji emoji,
                                  Long count,
                                  Boolean isClicked) {
    public static ThreadEmojiResponse from(Emoji emoji, Long count, Boolean isClicked) {
        return new ThreadEmojiResponse(emoji, count, isClicked);
    }
}
