package com.fourttttty.corookie.thread.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fourttttty.corookie.global.exception.InvalidEmojiException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Emoji {
    GOOD("good"),
    SMILE("smile"),
    BAD("bad");

    private final String value;

    @JsonCreator
    public static Emoji from(String value) {
        for (Emoji emoji : Emoji.values()) {
            if (emoji.getValue().equals(value)) {
                return emoji;
            }
        }
        throw new InvalidEmojiException();
    }

    @JsonValue
    public String getValue() { return this.value; }
}
