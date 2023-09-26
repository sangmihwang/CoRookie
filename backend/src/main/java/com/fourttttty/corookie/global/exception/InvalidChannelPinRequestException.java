package com.fourttttty.corookie.global.exception;

import lombok.Getter;

@Getter
public class InvalidChannelPinRequestException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public InvalidChannelPinRequestException() {
        this(ExceptionCode.CHANNEL_PIN_REQUEST_INVALID);
    }

    public InvalidChannelPinRequestException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
