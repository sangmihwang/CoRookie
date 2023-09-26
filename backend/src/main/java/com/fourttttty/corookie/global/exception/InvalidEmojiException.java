package com.fourttttty.corookie.global.exception;

import lombok.Getter;

@Getter
public class InvalidEmojiException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public InvalidEmojiException() {
        this(ExceptionCode.EMOJI_INVALID);
    }

    public InvalidEmojiException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

}
